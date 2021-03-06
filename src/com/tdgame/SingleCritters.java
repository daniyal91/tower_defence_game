package com.tdgame;

import java.util.Random;

import javax.swing.ImageIcon;

/**
 * To implement single critters
 * @author TEAM 2
 *
 */
public class SingleCritters implements CritterStrategy{
	
	@Override
	public void startWave() {
		// TODO Auto-generated method stub
		Screen.noOfCritters+=5;
		Screen.waveType="Single";
		Screen.critters = new Critter[Screen.noOfCritters];
		Screen.critters2 = new Critter[Screen.noOfCritters];
		for(int i=0;i<Screen.critters.length;i++)
		{	
			int random = new Random().nextInt(100);
			//Critter(			       	 imgWidth,  imgHeight, imgX, imgY, rectX, rectY, healthSpace, health,      speed)
			Screen.critters[i] = new Critter(50,      50,       25,   0,    -15,   -60,      10,       100,    Screen.critterSpeed);
			Screen.critters2[i] = new Critter(50,     50,       10,  -15,     0,   -20,   0, random, Screen.critterSpeed);
			
		}
		if(Screen.isFirst){
			Screen.crittersImgs[0] = new ImageIcon("../res/critter.gif").getImage();
			Screen.crittersImgs[1] = new ImageIcon("../res/firee.gif").getImage();
			Screen.crittersImgs[2] = new ImageIcon("../res/laser.gif").getImage();
			//Screen.crittersImgs[3] = new ImageIcon("../res/laser2.gif").getImage();
		}
		Screen.isFirst=false;
	}

}
