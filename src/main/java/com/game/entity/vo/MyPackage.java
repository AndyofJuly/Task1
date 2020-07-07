package com.game.entity.vo;

import com.game.common.Const;
import com.game.entity.excel.EquipmentStatic;
import com.game.entity.excel.PotionStatic;
import com.game.entity.vo.BagGridVo;
import com.game.service.assis.RandomSort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * 背包类
 * @Author andy
 * @create 2020/5/21 20:04
 */
public class MyPackage {
    // 背包id
    private int packageId;
    // 背包容量，默认为6
    private int size;
    // 背包极品装备数量
    private int bestNum = 0;
    // 背包格子集合，格子id-每个格子对象
    private HashMap<Integer,BagGridVo> packageGridHashMap = new HashMap<Integer, BagGridVo>();

    // 药类可叠加，只计数即可
    // 背包中的药品，使用List集合，元素可重复
//    private HashMap<Integer,Potion> potionHashMap = new HashMap<Integer,Potion>();
    // 背包中的装备，使用map集合，元素不可重复
//    private HashMap<Integer,Equipment> packageEquipmentHashMap = new HashMap<Integer,Equipment>();
    //物品id和物品数量
    private HashMap<Integer,Integer> goodsHashMap = new HashMap<Integer,Integer>();

/*    public MyPackage(int size, HashMap<Integer, Potion> potionHashMap, HashMap<Integer, Equipment> packageEquipmentHashMap) {
        this.size = size;
        this.potionHashMap = potionHashMap;
        this.packageEquipmentHashMap = packageEquipmentHashMap;
    }*/

    public MyPackage(int size, HashMap<Integer, Integer> goodsHashMap) {
        this.size = size;
        this.goodsHashMap = goodsHashMap;
        for(int i=0;i<size;i++){
            BagGridVo bagGridVo=new BagGridVo(i);
            packageGridHashMap.put(i,bagGridVo);
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public HashMap<Integer, Integer> getGoodsHashMap() {
        return goodsHashMap;
    }

    public void setGoodsHashMap(HashMap<Integer, Integer> goodsHashMap) {
        this.goodsHashMap = goodsHashMap;
    }
/*    public HashMap<Integer, Potion> getPotionHashMap() {
        return potionHashMap;
    }

    public void setPotionHashMap(HashMap<Integer, Potion> potionHashMap) {
        this.potionHashMap = potionHashMap;
    }

    public HashMap<Integer, Equipment> getPackageEquipmentHashMap() {
        return packageEquipmentHashMap;
    }

    public void setPackageEquipmentHashMap(HashMap<Integer, Equipment> packageEquipmentHashMap) {
        this.packageEquipmentHashMap = packageEquipmentHashMap;
    }*/

    public int getBestNum() {
        return bestNum;
    }

    public void setSumBestNum(int bestNum) {
        this.bestNum = this.bestNum+bestNum;
    }

    public HashMap<Integer, BagGridVo> getPackageGridHashMap() {
        return packageGridHashMap;
    }

    public void setPackageGridHashMap(HashMap<Integer, BagGridVo> packageGridHashMap) {
        this.packageGridHashMap = packageGridHashMap;
    }

    //查看背包时，考虑药品数量大于99，装备数量大于1时的情况，并且要计算倍数
    //对物品集合随机散落在格子上
    public void randPackageGrid(){
        clear();
        int[] arr = RandomSort.randSort(size);
        int k=0;
        for(Integer goodsId : goodsHashMap.keySet()){
            int number = goodsHashMap.get(goodsId);
            if(number!=0){
                //分药品和装备两种情况讨论
                if((goodsId+"").startsWith(Const.POTION_HEAD) && number>99){
                    //计算99的倍数
                    while (number>99){
                        packageGridHashMap.get(arr[k]).setGoodsId(goodsId);
                        packageGridHashMap.get(arr[k]).setNumber(99);
                        number-=99;
                        k++;
                    }
                    packageGridHashMap.get(arr[k]).setGoodsId(goodsId);
                    packageGridHashMap.get(arr[k]).setNumber(number);
                    k++;
                }else if((goodsId+"").startsWith(Const.EQUIPMENT_HEAD) && number>1){
                    while (number>1){
                        packageGridHashMap.get(arr[k]).setGoodsId(goodsId);
                        packageGridHashMap.get(arr[k]).setNumber(1);
                        number-=1;
                        k++;
                    }
                    packageGridHashMap.get(arr[k]).setGoodsId(goodsId);
                    packageGridHashMap.get(arr[k]).setNumber(number);
                    k++;
                }else{
                    packageGridHashMap.get(arr[k]).setGoodsId(goodsId);
                    packageGridHashMap.get(arr[k]).setNumber(number);
                    k++;
                }
            }
            System.out.println("数字"+k);
        }
    }

    //整理背包，规整地落在格子上
    public void orderPackageGrid(){
        clear();
        int[] arr = RandomSort.randSort(size);
        int k=0;
        for(Integer goodsId : goodsHashMap.keySet()){
            int number = goodsHashMap.get(goodsId);
            if(number!=0){
                //分药品和装备两种情况讨论
                if((goodsId+"").startsWith(Const.POTION_HEAD) && number>99){
                    //计算99的倍数
                    while (number>99){
                        packageGridHashMap.get(k).setGoodsId(goodsId);
                        packageGridHashMap.get(k).setNumber(99);
                        number-=99;
                        k++;
                    }
                    packageGridHashMap.get(k).setGoodsId(goodsId);
                    packageGridHashMap.get(k).setNumber(number);
                    k++;
                }else if((goodsId+"").startsWith(Const.EQUIPMENT_HEAD) && number>1){
                    while (number>1){
                        packageGridHashMap.get(k).setGoodsId(goodsId);
                        packageGridHashMap.get(k).setNumber(1);
                        number--;
                        k++;
                    }
                    packageGridHashMap.get(k).setGoodsId(goodsId);
                    packageGridHashMap.get(k).setNumber(number);
                    k++;
                }else{
                    packageGridHashMap.get(k).setGoodsId(goodsId);
                    packageGridHashMap.get(k).setNumber(number);
                    k++;
                }
            }
        }
    }

    private void clear(){
        for(int i=0;i<size;i++){
            packageGridHashMap.get(i).setGoodsId(0);
            packageGridHashMap.get(i).setNumber(0);
        }
    }

    public String getPackageGrid(){
        String packageInfo = "背包格子物品有：\n";
        for(Integer gridId : packageGridHashMap.keySet()){
            packageInfo+="格子:"+gridId+" 物品id:"+packageGridHashMap.get(gridId).getGoodsId()+" 数量:"+packageGridHashMap.get(gridId).getNumber()+"\n";
        }
        return packageInfo;
    }

    //检查背包是否还放得下，用在Service中put方法里检查
    public boolean checkIfCanPut(int goodsId,int number){
        boolean noSpace = true;
        for(int i=0;i<size;i++){
            if(packageGridHashMap.get(i).getGoodsId()==0){
                noSpace = false;
            }
        }
        if(noSpace){//没空间下同id是否还容得下
            if((goodsId+"").startsWith(Const.EQUIPMENT_HEAD)){
                return false;
            }else {
                for(int i=0;i<size;i++){
                    if(packageGridHashMap.get(i).getGoodsId() == goodsId && packageGridHashMap.get(i).getNumber()+number>99){
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
