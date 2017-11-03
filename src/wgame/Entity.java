
package wgame;

public class Entity
{
    public String avatar;
    public int hp, dmg;
/**
 * 
 * @param avatar string
 * @param hp int
 * @param dmg int
 */
    public Entity(String avatar, int hp, int dmg)
    {
        this.avatar = avatar;
        this.hp = hp;
        this.dmg = dmg;
    }
/**
 * empty
 */
    public Entity()
    {
        avatar = " ";
        hp  = 0;
        dmg = 0;
    }
    /**
     * is alive ?
     * @return int
     */
    public boolean isAlive() { return hp > 0; }
    
    public String toString() { return "[ "+avatar+" ]"; }
    
    
}