package com.game.system.bag.pojo;

import com.game.common.Const;
import com.game.system.assist.AssistService;

import java.util.HashMap;

/**
 * 背包类
 * @Author andy
 * @create 2020/5/21 20:04
 */
public class MyPackageBo {
    // 背包容量，默认为6
    private int size;
    // 背包极品装备数量
    private int bestNum = 0;
    // 背包格子集合，格子id-每个格子对象
    private HashMap<Integer, BagGridBo> packageGridHashMap = new HashMap<Integer, BagGridBo>();

    //物品id和物品数量
    private HashMap<Integer,Integer> goodsHashMap = new HashMap<Integer,Integer>();

    public MyPackageBo(int size, HashMap<Integer, Integer> goodsHashMap) {
        this.size = size;
        this.goodsHashMap = goodsHashMap;
        for(int i=0;i<size;i++){
            BagGridBo bagGridBo =new BagGridBo(i);
            packageGridHashMap.put(i, bagGridBo);
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public HashMap<Integer, Integer> getGoodsHashMap() {
        return goodsHashMap;
    }

    public void setGoodsHashMap(HashMap<Integer, Integer> goodsHashMap) {
        this.goodsHashMap = goodsHashMap;
    }

    public int getBestNum() {
        return bestNum;
    }

    public void setSumBestNum() {
        this.bestNum = this.bestNum+1;
    }

    public HashMap<Integer, BagGridBo> getPackageGridHashMap() {
        return packageGridHashMap;
    }

    public void setPackageGridHashMap(HashMap<Integer, BagGridBo> packageGridHashMap) {
        this.packageGridHashMap = packageGridHashMap;
    }

    //查看背包时，考虑药品数量大于99，装备数量大于1时的情况，并且要计算倍数
    //对物品集合随机散落在格子上
    public void randPackageGrid(){
        clear();
        int[] arr = AssistService.randSort(size);
        int k=0;
        for(Integer goodsId : goodsHashMap.keySet()){
            int number = goodsHashMap.get(goodsId);
            if(number!=0 && k<size){
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
        }
    }

    //整理背包，规整地落在格子上
    public void orderPackageGrid(){
        clear();
        int k=0;
        for(Integer goodsId : goodsHashMap.keySet()){
            int number = goodsHashMap.get(goodsId);
            if(number!=0 && k<size){
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

    //private void checkNum(){}

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
