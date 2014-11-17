package com.tdgame;

import java.awt.Color;
import java.awt.Image;

import javafx.beans.InvalidationListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Laser extends Tower{
	
	
	public Laser()
	{
		setBackground(Color.CYAN);
		//setTowerProperties(id, cost, ammunition, range, type,    rateOfFire,      path,           damageToCritters, level, weakstrategy)
		setTowerProperties(  id,  200,     1000,      1,  "Laser",   5,       "../res/towers/laser.png",     10,        1,     4);
		setIcon(new ImageIcon(this.imgPath));
	}
	
}
