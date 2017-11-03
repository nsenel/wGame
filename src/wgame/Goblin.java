/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgame;

public class Goblin extends Entity
{
    int goldDrop = 0;
    int Px,Py;
    /**
     * canavarin pozisyonu belirlenir
     * @param Px int
     * @param Py int
     */
    public Goblin(int Px , int Py)
    {
        hp = 90;
        dmg = 35;
        avatar = "\u25b2";
        goldDrop = 30 + (int)(Math.random() * 31);
        this.Px=Px;
        this.Py=Py;
    }
}
