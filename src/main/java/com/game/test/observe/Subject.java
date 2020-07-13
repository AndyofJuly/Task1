package com.game.test.observe;

import java.util.ArrayList;
import java.util.List;

public class Subject {
   
   //private List<Observer> observers = new ArrayList<Observer>();
   private Observer observer;
   private int state;
 
   public int getState() {
      return state;
   }
 
   public void setState(int state) {
      this.state = state;
      notifyAllObservers();
   }
 
/*   public void attach(Observer observer){
      observers.add(observer);
   }*/

   public void attach(Observer observer){
      this.observer = observer;
   }
 
   public void notifyAllObservers(){
/*      for (Observer observer : observers) {
         observer.update();
      }*/
      observer.update();
   }  
}