package com.game.system.bag;

import com.game.system.gameserver.GlobalInfo;
import com.game.common.Const;
import com.game.system.achievement.observer.BestEquipOb;
import com.game.system.achievement.observer.BodyEquipLvOb;
import com.game.system.achievement.observer.SumMoneyOb;
import com.game.system.achievement.entity.Subject;
import com.game.system.gameserver.AssistService;
import com.game.system.bag.entity.Equipment;
import com.game.system.bag.entity.EquipmentResource;
import com.game.system.bag.entity.EquipmentStatic;
import com.game.system.role.RoleService;
import com.game.system.role.entity.Role;
import org.springframework.stereotype.Service;


/**
 * 背包模块的业务逻辑处理
 * @Author andy
 * @create 2020/6/17 12:11
 */
@Service
public class PackageService {

    /**
     * 维修身上穿戴的装备-free
     * @param equipmentId 装备id
     * @param role 角色
     * @return 信息提示
     */
    public String repairEquipment(int equipmentId,Role role){
        if(!role.getEquipmentHashMap().containsValue(equipmentId)){
            return "没有此装备";
        }
        Equipment equipment = GlobalInfo.getEquipmentHashMap().get(equipmentId);
        equipment.setDura(AssistService.getEquipmentDura(equipmentId));

        return Const.Service.REPAIR_SUCCESS +equipment.getDura();
    }

    /**
     * 穿戴装备-从背包里拿出来穿戴-身上已有装备与背包互换
     * @param equipmentId 装备id
     * @param role 角色
     * @return 信息提示
     */
    public String putOnEquipment(int equipmentId,Role role){
        int staticId = AssistService.getStaticEquipId(equipmentId);
        EquipmentStatic equipmentInfo = EquipmentResource.getEquipmentStaticHashMap().get(staticId);
        putOnAffect(equipmentInfo,role);
        //判断装备类型，根据身上是否穿戴，与背包进行交换
        boolean alreadyWear = false;
        int wearPosition = equipmentInfo.getType();
        for(Integer key : role.getEquipmentHashMap().keySet()){
            int tempId = AssistService.getStaticEquipId(role.getEquipmentHashMap().get(key));
            if(equipmentInfo.getType().equals(EquipmentResource.getEquipmentStaticHashMap().get(tempId).getType())){
                alreadyWear = true;
                wearPosition=key;
            }
        }
        getFromPackage(equipmentId,1,role);

        if(alreadyWear){
            takeOffEquipment(wearPosition,role);
        }
        role.getEquipmentHashMap().put(wearPosition,equipmentId);

        bodyEquipLvSubject.notifyObserver(0,role);
        return Const.Service.PUT_ON_SUCCESS;
    }

    /**
     * 脱下装备 takeOff 0 表示脱掉装备槽为0的装备-武器
     * @param wearPosition 装备在身上的位置
     * @param role 角色
     * @return 信息提示
     */
    public String takeOffEquipment(int wearPosition,Role role){
        int equipmentId = role.getEquipmentHashMap().get(wearPosition);
        takeOffAffect(equipmentId,role);
        role.getEquipmentHashMap().remove(wearPosition);
        putIntoPackage(equipmentId,1,role);
        return Const.Service.TAKEOFF_SUCCESS;
    }

    /**
     * 穿戴装备时的增益；血量和蓝量增益为最大值增益，同时气血也增加
     * @param equipmentInfo 包含装备属性的装备静态类
     * @param role 角色
     */
    private void putOnAffect(EquipmentStatic equipmentInfo,Role role){
        if(equipmentInfo.getType()==0){
            role.setAtk(role.getAtk() + equipmentInfo.getAtk());
        }else if(equipmentInfo.getType()==1){
            role.setMaxHp(role.getMaxHp()+equipmentInfo.getAddHp());
            RoleService.checkAndSetHp(role.getHp()+equipmentInfo.getAddHp(),role);
        }else if(equipmentInfo.getType()==2){
            role.setMaxMp(role.getMaxMp()+equipmentInfo.getAddMp());
            RoleService.checkAndSetMp(role.getMp()+equipmentInfo.getAddMp(),role);
        }else if(equipmentInfo.getType()==3){
            role.setDef(role.getDef()+equipmentInfo.getAddDef());
        }
    }

    /**
     * 脱掉装备时去掉增益；血量和蓝量最大值减小，血量始终约束在最大值范围内
     * @param equipmentId 装备唯一id
     * @param role 角色
     */
    private void takeOffAffect(int equipmentId,Role role){
        int staticId = AssistService.getStaticEquipId(equipmentId);
        EquipmentStatic equipmentInfo = EquipmentResource.getEquipmentStaticHashMap().get(staticId);
        if(equipmentInfo.getType()==0){
            role.setAtk(role.getAtk() - equipmentInfo.getAtk());
        }else if(equipmentInfo.getType()==1){
            role.setMaxHp(role.getMaxHp()-equipmentInfo.getAddHp());
            RoleService.checkAndSetHp(role.getHp(),role);
        }else if(equipmentInfo.getType()==2){
            role.setMaxMp(role.getMaxMp()-equipmentInfo.getAddMp());
            RoleService.checkAndSetMp(role.getMp(),role);
        }else if(equipmentInfo.getType()==3){
            role.setDef(role.getDef()-equipmentInfo.getAddDef());
        }
    }

    /**
     * 获得身穿武器的当前耐久
     * @param role 角色
     * @return 信息提示
     */
    public String getWeaponDura(Role role){
        if(role.getEquipmentHashMap().get(0)==0){
            return "没有佩戴武器！";
        }
        Equipment equipment = GlobalInfo.getEquipmentHashMap().get(role.getEquipmentHashMap().get(0));
        return "当前武器耐久："+ equipment.getDura();
    }

    /**
     * 使用药品
     * @param potionId 药品id
     * @param role 角色
     * @return 是否成功使用
     */
    public String useDrug(int potionId,Role role){
        int hp = role.getHp();
        int mp = role.getMp();
        if(!getFromPackage(potionId,1,role)){
            return Const.Service.USE_FAILURE;
        }
        RoleService.checkAndSetHp(hp + AssistService.getPotionAddHp(potionId),role);
        RoleService.checkAndSetMp(mp + AssistService.getPotionAddMp(potionId),role);
        int recoverHp = role.getHp() - hp;
        int recoverMp = role.getMp() - mp;
        return Const.Service.USE_SUCCESS+"，血量增加"+recoverHp+"，蓝量增加"+recoverMp;
    }

    /**
     * 获取背包物品信息-简略
     * @param role 角色
     * @return 信息提示
     */
    public static String getPackage(Role role){
        StringBuilder list= new StringBuilder();
        for(Integer goodsId : role.getMyPackageBo().getGoodsHashMap().keySet()){
            int num = role.getMyPackageBo().getGoodsHashMap().get(goodsId);
            if(num>0){
                list.append(goodsId+"-"+num).append(" ");
            }
        }
        return list.toString();
    }

    /**
     * 获得身上装备信息-简略
     * @param role 角色
     * @return 信息提示
     */
    public static String getBodyEquip(Role role){
        StringBuilder list= new StringBuilder();
        for(Integer key : role.getEquipmentHashMap().keySet()){
            int weaponId = role.getEquipmentHashMap().get(key);
            Equipment equipment = GlobalInfo.getEquipmentHashMap().get(weaponId);
            list.append(weaponId+"-"+equipment.getEquipmentId()).append(" ");
        }
        return list.toString();
    }

    /**
     * 整理背包
     * @param role 角色
     * @return 信息提示
     */
    public String orderPackage(Role role){
        role.getMyPackageBo().orderPackageGrid();
        return getPackageInfo(role);
    }

    /**
     * 获得背包中每个格子的信息
     * @param role 角色
     * @return 信息提示
     */
    public String getPackageInfo(Role role){
        return role.getMyPackageBo().getPackageGrid();
    }

    /**
     * 将物品放入背包
     * @param goodsId 物品id
     * @param number 数量
     * @param role 角色
     * @return boolean
     */
    public boolean putIntoPackage(int goodsId,int number,Role role){
        if(!role.getMyPackageBo().checkIfCanPut(goodsId, number)){
            return false;
        }

        if(role.getMyPackageBo().getGoodsHashMap().get(goodsId)!=null){
            int allAmount = role.getMyPackageBo().getGoodsHashMap().get(goodsId)+number;
            role.getMyPackageBo().getGoodsHashMap().put(goodsId,allAmount);
        }else {
            role.getMyPackageBo().getGoodsHashMap().put(goodsId,number);
        }

        if(String.valueOf(goodsId).startsWith(Const.EQUIPMENT_HEAD)){
            int staticId = AssistService.getStaticEquipId(goodsId);
            int quality = EquipmentResource.getEquipmentStaticHashMap().get(staticId).getQuality();
            if(quality==1){
                bestEquipSubject.notifyObserver(0,role);
            }
        }
        return true;
    }

    /**
     * 将物品从背包中取出
     * @param goodsId 物品id
     * @param number 数量
     * @param role 角色
     */
    public boolean getFromPackage(int goodsId,int number,Role role){
        int leftAmount = role.getMyPackageBo().getGoodsHashMap().get(goodsId)-number;
        if(leftAmount<0){
            return false;
        }else{
            role.getMyPackageBo().getGoodsHashMap().put(goodsId,leftAmount);
            role.getMyPackageBo().orderPackageGrid();
            return true;
        }
    }

    /**
     * 失去金钱
     * @param number 金钱数量
     * @param role 角色
     */
    public void addMoney(int number,Role role){
        role.setMoney(role.getMoney()+number);
        sumMoneySubject.notifyObserver(number,role);
    }

    /**
     * 获得金钱
     * @param number 金钱数量
     * @param role 角色
     */
    public boolean lostMoney(int number,Role role){
        int leftMoney = role.getMoney()-number;
        if(leftMoney<0){
            return false;
        }
        role.setMoney(leftMoney);
        return true;
    }

    /** 注册成就观察者 */
    Subject bestEquipSubject = new Subject();
    Subject sumMoneySubject = new Subject();
    Subject bodyEquipLvSubject = new Subject();
    private BestEquipOb bestEquipOb = new BestEquipOb(bestEquipSubject);
    private SumMoneyOb sumMoneyOb = new SumMoneyOb(sumMoneySubject);
    private BodyEquipLvOb bodyEquipLvOb = new BodyEquipLvOb(bodyEquipLvSubject);

    //饿汉式单例模式
    private static PackageService instance = new PackageService();
    private PackageService() {}
    public static PackageService getInstance() {
        return instance;
    }

}
