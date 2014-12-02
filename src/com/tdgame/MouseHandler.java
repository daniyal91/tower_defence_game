package com.tdgame;

import java.awt.HeadlessException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * This class is responsible for handling all the mouse related events
 * 
 * @author Team 2
 */

public class MouseHandler implements MouseListener, MouseMotionListener{

	/* variables to handle mouse events */
	private static Screen screen;
	private Screen.MouseHeld mouseHeld;
	static int count=0;
	static int x;
	static int y;
	int previousX;
	int previousY;
	boolean createMap;
	boolean startPointDone;	
	public static boolean mapCompleted;	
	int userSelectionX;
	int userSelectionY;
	int boxNumberX;
	int boxNumberY;
	int boxNumberX_plus_1;
	int boxNumberX_minus_1;
	int boxNumberY_plus_1;
	int boxNumberY_minus_1;
	public static boolean rightClick = false;
	static int largestPositionValue_inMap=0;
	private static boolean editMap;	
	private static String arrayIndex;
	public static String startPoint_while_loadingMap;
	public static String endPoint_while_loadingMap;
	public static boolean saveEditedMap;
	public static boolean saveGame = false;
	
	/* Data structure variables */
	static LinkedList<String> arrayList_to_hold_occupied_blocks;
	static LinkedList<String> path_completion_detection;
	LinkedList<String> nextBlock;
	LinkedList<String> previousSurroundingBlocks;
	static TreeMap<Integer, String> hashMap_of_pathIndex_with_position;
	public static HashMap<String, Integer> boxPositionPathNumberMap = new HashMap<String, Integer>();

	
	public MouseHandler(Screen screen, int x, int y, boolean createMap) {
		this.screen = screen;
		this.x = x;
		this.y = y;
		this.createMap = createMap;
		this.mouseHeld = this.screen.new MouseHeld(); 
		arrayList_to_hold_occupied_blocks = new LinkedList<String>();
		path_completion_detection = new LinkedList<String>();
		nextBlock = new LinkedList<String>();
		previousSurroundingBlocks = new LinkedList<String>();
		hashMap_of_pathIndex_with_position = new TreeMap<Integer, String>();
		mapCompleted = false;
		editMap = false;
		saveEditedMap = false;
	}
	
	public MouseHandler(Screen screen) {
		this.screen = screen;
		this.mouseHeld = this.screen.new MouseHeld(); 
	}

	// This method deals with taking actions based upon mouse drag 
	public void mouseDragged(MouseEvent e) {
		
		if(createMap && !mapCompleted && Screen.typeOfOperation.equals("createMap")) {
			this.mouseHeld.createPath = true;
			createOwnMap(e);
		}
		else if(Screen.typeOfOperation.equals("loadMap") && editMap) {			
			createOwnMap(e);
		}
		else
		{
			getBoxNumber(e);					
			arrayIndex = boxNumberX+"_"+ boxNumberY;
						
			if ((userSelectionX <= (x * 50) + 50 && userSelectionX > 50) && (userSelectionY <= (y * 50) + 50 && userSelectionY > 50)) {				
				
				if(Screen.typeOfOperation.equals("loadMap")) {
					//here we are clearing onMapTowerPropTbl if user clicks on map instead of tower
					screen.onMapTowerPropTbl.setValueAt("", 0, 1);
					screen.onMapTowerPropTbl.setValueAt("", 1, 1);
					screen.onMapTowerPropTbl.setValueAt("", 2, 1);
					screen.onMapTowerPropTbl.setValueAt("", 3, 1);
					screen.onMapTowerPropTbl.setValueAt("", 4, 1);
					screen.onMapTowerPropTbl.setValueAt("", 5, 1);
					screen.onMapTowerPropTbl.setValueAt("", 6, 1);
					JOptionPane.showMessageDialog(null, "Inorder to edit it, click on Edit Map.");
					
				}
				else if(!editMap){
					JOptionPane.showMessageDialog(null, "Your map is complete. Either save it or create a new map!");
				}
				
			}
		}
	}

	public void mouseMoved(MouseEvent e) {
		
	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		
	}

	// This method deals with taking actions based upon mouse release 
	public void mouseReleased(MouseEvent e) {				
		
		if(createMap && !mapCompleted && Screen.typeOfOperation.equals("createMap")) {
			this.mouseHeld.createPath = true;
			rightClick = false;
			createOwnMap(e);
		}
		else if(Screen.typeOfOperation.equals("loadMap") && editMap) {			
			createOwnMap(e);
		}
		else
		{
			getBoxNumber(e);					
			arrayIndex = boxNumberX+"_"+ boxNumberY;
						
			if ((userSelectionX <= (x * 50) + 50 && userSelectionX > 50) && (userSelectionY <= (y * 50) + 50 && userSelectionY > 50)) {				
				
				if(Screen.typeOfOperation.equals("loadMap")) {
					JOptionPane.showMessageDialog(null, "Inorder to edit it, click on Edit Map.");
				}
				else if(!editMap){
					JOptionPane.showMessageDialog(null, "Your map is complete. Either save it or create a new map!");
				}
				
			}
		}
	}
	
	// This method gives the array index based upon the screen coordinates
	private void getBoxNumber(MouseEvent e) {
		userSelectionX = e.getXOnScreen();
		userSelectionY = e.getYOnScreen();
//		System.exit(0);
		// get the index number based upon the x - y coordinates of the mouse click
		boxNumberX = (userSelectionX / 50) - 1;
		boxNumberY = (userSelectionY / 50) - 1;
	}
	
	// This method takes care of selection of start and end point
	private void selectStartPoint_and_EndPoint(MouseEvent e) {
		
		if((boxNumberX == 0 || boxNumberY == 0) || 
				(boxNumberX == x - 1 || boxNumberY == y - 1)) {
			
			// Store the surrounding blocks of finish point in an array to see if the path has been finished
			if(count == 1) {
				
				boxNumberX_plus_1 = boxNumberX + 1;
				boxNumberX_minus_1 = boxNumberX - 1;
				boxNumberY_plus_1 = boxNumberY + 1;
				boxNumberY_minus_1 = boxNumberY - 1;
				
				path_completion_detection.add(boxNumberX_plus_1 +"_"+boxNumberY);
				path_completion_detection.add(boxNumberX_minus_1 +"_"+boxNumberY);
				path_completion_detection.add(boxNumberX +"_"+boxNumberY_plus_1);
				path_completion_detection.add(boxNumberX +"_"+ boxNumberY_minus_1);
				
				startPointDone = true;
				
			}
			// to decide where exactly the next block should come in after start point
			else if(count == 0) {
				
				boxNumberX_plus_1 = boxNumberX + 1;
				boxNumberX_minus_1 = boxNumberX - 1;
				boxNumberY_plus_1 = boxNumberY + 1;
				boxNumberY_minus_1 = boxNumberY - 1;
				
				nextBlock.add(boxNumberX_plus_1 +"_"+boxNumberY);
				nextBlock.add(boxNumberX_minus_1 +"_"+boxNumberY);
				nextBlock.add(boxNumberX +"_"+boxNumberY_plus_1);
				nextBlock.add(boxNumberX +"_"+ boxNumberY_minus_1);
			}
			
			count++;
			arrayList_to_hold_occupied_blocks.add(arrayIndex);					
			mouseHeld.mouseDown(e, count, boxNumberX, boxNumberY);
		}
	}

	/**
	 * This method is called when the user wants to create map or edit map
	 * 
	 * @param e MouseEvent
	 */
	private void createOwnMap(MouseEvent e) {
		
		getBoxNumber(e);					
		
		// hold the string version of array index where mouse was released
		arrayIndex = boxNumberX+"_"+ boxNumberY;
		
		// this condition is to limit the mouse released detection to the map
		if ((userSelectionX <= (x * 50) + 50 && userSelectionX > 50) && (userSelectionY <= (y * 50) + 50 && userSelectionY > 50)) {				
			// count 0 means Starting point, count 1 means ending point
			// this check is to make sure that the starting & ending point are placed on the borders of the map
			if(count == 0 || count == 1) {
				selectStartPoint_and_EndPoint(e);
			}
			// when starting & ending points are done, go to else
			else {
				
				if(SwingUtilities.isRightMouseButton(e)) {
					rightClick = false;
					if(!arrayIndex.equals(startPoint_while_loadingMap) && !arrayIndex.equals(endPoint_while_loadingMap)) {
						deselectPathPoints(e);
					}
					else {
						JOptionPane.showMessageDialog(null, "Sorry, but you can't change the start/end point!");
					}
				}
				else if(SwingUtilities.isLeftMouseButton(e) && !rightClick) {
					selectPathPoints(e);
				}								
								
				// check if path is completed
				if(path_completion_detection.contains(arrayIndex) && arrayList_to_hold_occupied_blocks.contains(arrayIndex)) {
					String userReply = mouseHeld.pathCompleted(screen);

					if(userReply.equalsIgnoreCase("YES")) {
						mapCompleted  = true;
						ActionHandler.mapCreated = true;
					}	
					else {
						rightClick = true;
					}
				}
			}			
		}		
	}
	
	// this method is used to delete the map path on right click
	private void deselectPathPoints(MouseEvent e) {

		if((arrayList_to_hold_occupied_blocks.get(arrayList_to_hold_occupied_blocks.size()-1)).equals(arrayIndex)) {
			
			arrayList_to_hold_occupied_blocks.removeLastOccurrence(arrayIndex);
			boxPositionPathNumberMap.remove(arrayIndex);
			for (Entry<Integer, String> entry : hashMap_of_pathIndex_with_position.entrySet()) {
	            if (entry.getValue().equals(arrayIndex)) {
	            	hashMap_of_pathIndex_with_position.remove(entry.getKey());
	            }
	        }
			
			mouseHeld.mouseDown(e, count, boxNumberX, boxNumberY);
			
			nextBlock.clear();
			
			for(String next: previousSurroundingBlocks) {
				nextBlock.add(next);
			}
			
			previousSurroundingBlocks.clear();
			
			try {
				String secondLastElement = arrayList_to_hold_occupied_blocks.get(arrayList_to_hold_occupied_blocks.size() - 2);
				
				int split_arrayIndex_X = Integer.parseInt(secondLastElement.split("_")[0]);
				int split_arrayIndex_Y = Integer.parseInt(secondLastElement.split("_")[1]);
				
				previousSurroundingBlocks.add((split_arrayIndex_X+1) +"_"+split_arrayIndex_Y);
				previousSurroundingBlocks.add((split_arrayIndex_X-1) +"_"+split_arrayIndex_Y);
				previousSurroundingBlocks.add(split_arrayIndex_X +"_"+ (split_arrayIndex_Y+1));
				previousSurroundingBlocks.add(split_arrayIndex_X +"_"+ (split_arrayIndex_Y-1));
			} catch (Exception ex) {}
			
		}
		
	}

	// this method is use to create the map path on left click
	private void selectPathPoints(MouseEvent e) {

		if(nextBlock.contains(arrayIndex) && !arrayList_to_hold_occupied_blocks.contains(arrayIndex)) {
			count++;
			arrayList_to_hold_occupied_blocks.add(arrayIndex);
			hashMap_of_pathIndex_with_position.put(count, arrayIndex);
			boxPositionPathNumberMap.put(arrayIndex, count);
			mouseHeld.mouseDown(e, count, boxNumberX, boxNumberY);
			
			boxNumberX_plus_1 = boxNumberX + 1;
			boxNumberX_minus_1 = boxNumberX - 1;
			boxNumberY_plus_1 = boxNumberY + 1;
			boxNumberY_minus_1 = boxNumberY - 1;
			
			previousSurroundingBlocks.clear();
			
			for(String previousBlock: nextBlock) {
				previousSurroundingBlocks.add(previousBlock);
			}
			
			nextBlock.clear();
			nextBlock.add(boxNumberX_plus_1 +"_"+boxNumberY);
			nextBlock.add(boxNumberX_minus_1 +"_"+boxNumberY);
			nextBlock.add(boxNumberX +"_"+boxNumberY_plus_1);
			nextBlock.add(boxNumberX +"_"+ boxNumberY_minus_1);
		}
		
	}
	
	// this method save the map with the current state
	public void saveGame() {
		
		try {
			if(saveGame && !Screen.isWaveRunning) {// && path_completion_detection.contains(arrayIndex) && arrayList_to_hold_occupied_blocks.contains(arrayIndex)) {
				
				String userReply = mouseHeld.pathCompleted(screen);
				
				if(userReply.equalsIgnoreCase("YES")) {
					mapCompleted  = true;
					ActionHandler.mapCreated = true;
				}
				else {
					rightClick = true;
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "No valid state to save");
			}
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, "No valid state to save");
		}
	}

	// this method saves the map based upon the menu option
	public void saveMapByMenu() {
		
		if(!saveGame && "createMap".equals(Screen.typeOfOperation) || saveEditedMap) {
			if(path_completion_detection.contains(arrayIndex) && arrayList_to_hold_occupied_blocks.contains(arrayIndex)) {
				String userReply = mouseHeld.pathCompleted(screen);
			
				if(userReply.equalsIgnoreCase("YES")) {
					mapCompleted  = true;
					ActionHandler.mapCreated = true;
				}
				else {
					rightClick = true;
				}
				
			}
			else {
				mouseHeld.incompleteMap();
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "You can only use this option when creating or editing a map");			
		}		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
	
	}

	// this method prepares the data structures and other variables for map editing
	public void mapReadyForEditing() {
		
		try {
			path_completion_detection = new LinkedList<String>();
			nextBlock = new LinkedList<String>();
			previousSurroundingBlocks = new LinkedList<String>();
			arrayList_to_hold_occupied_blocks.clear();
			
			arrayList_to_hold_occupied_blocks.add(startPoint_while_loadingMap);
			
			for (Entry<Integer, String> entry : hashMap_of_pathIndex_with_position.entrySet()) {
				arrayList_to_hold_occupied_blocks.add(entry.getValue());             
			}
			
			String lastElement = arrayList_to_hold_occupied_blocks.get(arrayList_to_hold_occupied_blocks.size() - 1);
			
			int split_arrayIndex_X = Integer.parseInt(lastElement.split("_")[0]);
			int split_arrayIndex_Y = Integer.parseInt(lastElement.split("_")[1]);
			
			nextBlock.clear();
			
			nextBlock.add((split_arrayIndex_X+1) +"_"+split_arrayIndex_Y);
			nextBlock.add((split_arrayIndex_X-1) +"_"+split_arrayIndex_Y);
			nextBlock.add(split_arrayIndex_X +"_"+ (split_arrayIndex_Y+1));
			nextBlock.add(split_arrayIndex_X +"_"+ (split_arrayIndex_Y-1));
			
			String secondLastElement = arrayList_to_hold_occupied_blocks.get(arrayList_to_hold_occupied_blocks.size() - 2);
			
			split_arrayIndex_X = Integer.parseInt(secondLastElement.split("_")[0]);
			split_arrayIndex_Y = Integer.parseInt(secondLastElement.split("_")[0]);
			
			previousSurroundingBlocks.add((split_arrayIndex_X+1) +"_"+split_arrayIndex_Y);
			previousSurroundingBlocks.add((split_arrayIndex_X-1) +"_"+split_arrayIndex_Y);
			previousSurroundingBlocks.add(split_arrayIndex_X +"_"+ (split_arrayIndex_Y+1));
			previousSurroundingBlocks.add(split_arrayIndex_X +"_"+ (split_arrayIndex_Y-1));
			
			// For detection of end point
			int split_endPoint_X = Integer.parseInt(endPoint_while_loadingMap.split("_")[0]);
			int split_endPoint_Y = Integer.parseInt(endPoint_while_loadingMap.split("_")[1]);
			
			path_completion_detection.add((split_endPoint_X+1) +"_"+split_endPoint_Y);
			path_completion_detection.add((split_endPoint_X-1) +"_"+split_endPoint_Y);
			path_completion_detection.add(split_endPoint_X +"_"+ (split_endPoint_Y+1));
			path_completion_detection.add(split_endPoint_X +"_"+ (split_endPoint_Y-1));
			
			editMap = true;
			count=largestPositionValue_inMap;
			
			Screen.instructions = "You chose to edit the map!";
		} catch (Exception e) {
		}
	}

}
