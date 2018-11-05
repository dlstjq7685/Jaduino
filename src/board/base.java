package board;

import tx_rx.serial;

public class base extends serial {

    public String name;

    public base(){
        this.name = "Jaduino";
    }

    public base(String name){
        this.name = name;
    }

    public boolean digital_write(int pin){
        return true;
    }

    public boolean digital_read(int pin, int read){
        return true;
    }

    public boolean analog_write(int pin){
        return true;
    }

    public boolean analog_read(int pin, int read){
        return true;
    }



    public static void main(String[] args){
        base b = new base();
        b.connect();
    }
}
