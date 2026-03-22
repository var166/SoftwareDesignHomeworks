 class Driver {
    private final Car car;
    public Driver(Car car){this.car = car;}
     public Driver(String s){
        this.car = new Car(s);
     }
}
class Car{
     String car;
     Car(String s){
         this.car = s;
     }
}