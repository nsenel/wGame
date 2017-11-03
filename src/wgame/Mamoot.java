/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wgame;

public class Mamoot extends Entity
{

    int goldDrop = 0;
    int Px,Py;
    /**
     * canavarin pozisyonu belirlenir
     * @param Px int
     * @param Py int
     */
    public Mamoot(int Px, int Py)
    {
        hp = 60;
        dmg = 15;
        avatar = "\u2126";
        goldDrop = 20 + (int)(Math.random() * 21);
        this.Px=Px;
        this.Py=Py;
    }
    
}
