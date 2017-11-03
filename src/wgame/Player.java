/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgame;

public class Player extends Entity
{
    public int goldCnt = 0, killCnt = 0, level = 1 ,Px=0, Py=0;
    
    public Player()
    {
        avatar = "\u263A";
        
        hp = 300;
        dmg = 25;
    }
    public void lvlup()
    {
        hp = hp + (int) (level *10);
        dmg = dmg + (int) (dmg * 0.2);
    }
}
