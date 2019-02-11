package tankwars;

/**
 *
 * @author psoderquist
 */
class TankAction {
    public TankAction(String action, Tank actingTank)
    {
        this.action = action;
        this.actingTank = actingTank;
    }
    public String action;
    public Tank actingTank;
}