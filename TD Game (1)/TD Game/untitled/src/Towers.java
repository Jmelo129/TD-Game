public class Towers {

    int x, y, range, firerate, dmg, target, bulletTimer=0, waitTime=0, bulletTargetX=0, bulletTargetY=0;

    Towers(int x, int y, int fr, int dmg, int r){
        this.x = x;
        this.y = y;
        firerate = fr;
        this.dmg = dmg;
        range=r;
    }

    // Getters and Setters

    int getBT() {
        return bulletTimer;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    int getRange() {
        return range;
    }

    int getFr() {
        return firerate;
    }

    int getDmg() {
        return dmg;
    }

    int getTarget() {
        return target;
    }
    int getWait() {
        return waitTime;
    }
    int getBulletTargetX() {
        return bulletTargetX;
    }
    int getBulletTargetY() {
        return bulletTargetY;
    }



    void setBT(int bt) {
        bulletTimer = bt;
    }

    void setX(int x) {
        this.x = x;
    }

    void setY(int y) {
        this.y = y;
    }

    void setFr(int fr) {
        firerate = fr;
    }

    void setDmg(int dmg) {
        this.dmg = dmg;
    }

    void setRange(int r) {
        range = r;
    }

    void setTarget(int t) {
        target = t;
    }
    void setWait(int wt) {
        waitTime = wt;
    }
    void setBulletTargetX(int bTX) {
        bulletTargetX = bTX;
    }
    void setBulletTargetY(int bTY) {
        bulletTargetY = bTY;
    }
}
