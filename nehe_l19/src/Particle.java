public class Particle {

    private boolean isActive;
    private float life;
    private float fade;
    private float r;
    private float g;
    private float b;
    private float x;
    private float y;
    private float z;
    private float xi;
    private float yi;
    private float zi;
    private float xg;
    private float yg;
    private float zg;

    public Particle(boolean isActive, float life, float fade, float r, float g, float b, float xi, float yi, float zi,
                    float xg, float yg, float zg) {

        this.isActive = isActive;
        this.life = life;
        this.fade = fade;
        this.r = r;
        this.g = g;
        this.b = b;
        this.xi = xi;
        this.yi = yi;
        this.zi = zi;
        this.xg = xg;
        this.yg = yg;
        this.zg = zg;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public float getLife() {
        return life;
    }

    public void setLife(float life) {
        this.life = life;
    }

    public float getFade() {
        return fade;
    }

    public void setFade(float fade) {
        this.fade = fade;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public float getG() {
        return g;
    }

    public void setG(float g) {
        this.g = g;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getXi() {
        return xi;
    }

    public void setXi(float xi) {
        this.xi = xi;
    }

    public float getYi() {
        return yi;
    }

    public void setYi(float yi) {
        this.yi = yi;
    }

    public float getZi() {
        return zi;
    }

    public void setZi(float zi) {
        this.zi = zi;
    }

    public float getXg() {
        return xg;
    }

    public void setXg(float xg) {
        this.xg = xg;
    }

    public float getYg() {
        return yg;
    }

    public void setYg(float yg) {
        this.yg = yg;
    }

    public float getZg() {
        return zg;
    }

    public void setZg(float zg) {
        this.zg = zg;
    }
}
