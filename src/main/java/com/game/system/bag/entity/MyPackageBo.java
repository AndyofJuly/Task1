package com.game.system.bag.entity;

import com.game.common.Const;

import java.util.HashMap;

/**
 * 背包类，物品进出都是通过操作集合，当角色查看背包时，才以格子的方式呈现
 * @Author andy
 * @create 2020/5/21 20:04
 */
public class MyPackageBo {
    /** 背包容量，默认为6*/
    private Integer size;
    /** 背包格子集合，格子id-每个格子对象-在该类中封装，仅对外提供信息，只有通过goodsHashMap才能对其操作*/
    private HashMap<Integer, BagGridBo> packageGridHashMap = new HashMap<Integer, BagGridBo>();
    /** 物品id和物品数量-外部访问时访问该集合，主要为了方便数据库读取*/
    private HashMap<Integer,Integer> goodsHashMap = new HashMap<Integer,Integer>();

    public MyPackageBo(Integer size, HashMap<Integer, Integer> goodsHashMap) {
        this.size = size;
        this.goodsHashMap = goodsHashMap;
        //初始化
        for(int i=0;i<size;i++){
            BagGridBo bagGridBo =new BagGridBo(i);
            packageGridHashMap.put(i, bagGridBo);
        }
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public HashMap<Integer, BagGridBo> getPackageGridHashMap() {
        return packageGridHashMap;
    }

    public void setPackageGridHashMap(HashMap<Integer, BagGridBo> packageGridHashMap) {
        this.packageGridHashMap = packageGridHashMap;
    }

    public HashMap<Integer, Integer> getGoodsHashMap() {
        return goodsHashMap;
    }

    public void setGoodsHashMap(HashMap<Integer, Integer> goodsHashMap) {
        this.goodsHashMap = goodsHashMap;
    }

    /** 整理背包，使物品规整地落在格子上 */
    public void orderPackageGrid(){
        clear();
        int k=0;
        int maxNum = Const.POTION_MAX_NUM;
        for(Integer goodsId : goodsHashMap.keySet()){
            int number = goodsHashMap.get(goodsId);
            if(number!=0 && k<size){
                //分药品和装备两种情况讨论-若有其他道具，还需要扩展并修改
                if((String.valueOf(goodsId)).startsWith(Const.POTION_HEAD) && number>maxNum){
                    //计算99的倍数
                    while (number>maxNum){
                        putIntoGrid(k,goodsId,maxNum);
                        number-=maxNum;
                        k++;
                        if(k>=size){break;}
                    }
                    putIntoGrid(k,goodsId,number);
                    k++;
                }else if((String.valueOf(goodsId)).startsWith(Const.EQUIPMENT_HEAD) && number>1){
                    while (number>1){
                        putIntoGrid(k,goodsId,1);
                        number--;
                        k++;
                        if(k>=size){break;}
                    }
                    putIntoGrid(k,goodsId,number);
                    k++;
                }else{
                    putIntoGrid(k,goodsId,number);
                    k++;
                }
            }
        }
    }

    //在指定格子位置放置物品和数量
    private void putIntoGrid(int id,int goodsId,int num){
        packageGridHashMap.get(id).setGoodsId(goodsId);
        packageGridHashMap.get(id).setNumber(num);
    }

    private void check(){

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

    //检查背包是否还放得下-为了使得packageGridHashMap不对外暴露，而放在该类中
    public boolean checkIfCanPut(int goodsId,int number){
        //格子与集合同步一下
        orderPackageGrid();
        boolean havSpace = false;
        for(int i=0;i<size;i++){
            if(packageGridHashMap.get(i).getGoodsId()==0){
                havSpace = true;
            }
        }
        if(havSpace){
            System.out.println("测试提示：背包还有空间放下该物品");
            return true;
        }else{
            if((String.valueOf(goodsId)).startsWith(Const.EQUIPMENT_HEAD)){
                return false;
            }else {
                for(int i=0;i<size;i++){
                    if(packageGridHashMap.get(i).getGoodsId() == goodsId && packageGridHashMap.get(i).getNumber()+number>Const.POTION_MAX_NUM){
                        return false;
                    }
                }
                return true;
            }
        }
    }
}
