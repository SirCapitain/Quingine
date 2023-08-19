package quingine.sim.cam;

public interface QuameraListener {
    void rotated(double yaw, double pitch, double roll);
    void moved(double x, double y, double z);
    void updated(double x, double y, double z, double yaw, double pitch, double roll);
}
