package qat;


/**
 * Quaternion class. Don't know what one is? Look it up.
 * Can probably do this without this class buuuuuuut.... im dum
 * @author Quinn Graham
 */

public class Quaternion {
    public double value;
    public String type;

    /**
     * First step in becoming cool.
     * Learn Quaternions.
     * That's why I so cool. right?
     * @param value defines how much
     * @param type defines what kind
     */
    public Quaternion(double value, String type){
        this.value = value;
        this.type = type;
    }
    public Quaternion(double value){
        this.value = value;
        this.type = null;
    }

    /**
     * Multiplying a quaternion is different than most people think.
     * For example. I*J = K but J*I = -K
     * Order matters
     * @param quat1 first quaternion
     * @param quat2 second quaternion
     * @return new quaternion
     */
    public static Quaternion multiplyQuaternion(Quaternion quat1, Quaternion quat2){
        if (quat1.type == null || quat2.type == null)
            if (quat1.type != null)
                return new Quaternion(quat1.value * quat2.value, quat1.type);
            else if (quat2.type != null)
                return new Quaternion(quat1.value * quat2.value, quat2.type);
            else
                return new Quaternion(quat1.value * quat2.value, null);
        if (quat1.type.equals(quat2.type))
            return new Quaternion(-(quat1.value* quat2.value), null);
        switch(quat1.type){
            case "i":
                switch (quat2.type){
                    case "j":
                        return new Quaternion(quat1.value * quat2.value, "k");
                    case"k":
                        return new Quaternion(-(quat1.value * quat2.value), "j");
                }
            case "j":
                switch (quat2.type){
                    case "i":
                        return new Quaternion(-(quat1.value * quat2.value), "k");
                    case"k":
                        return new Quaternion(quat1.value * quat2.value, "i");
                }
            case "k":
                switch (quat2.type){
                    case "i":
                        return new Quaternion(quat1.value * quat2.value, "j");
                    case"j":
                        return new Quaternion(-(quat1.value * quat2.value), "i");
                }
        }
        return null;
    }

    /**
     * The propper way to write a quaternion;
     * @return quaternion
     */
    @Override
    public String toString() {
        return value + type;
    }
}