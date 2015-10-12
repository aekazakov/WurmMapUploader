package com.wurmonline.wurmapi.api;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.wurmonline.mesh.BushData.BushType;
import com.wurmonline.mesh.FoliageAge;
import com.wurmonline.mesh.GrassData;
import com.wurmonline.mesh.Tiles.Tile;
import com.wurmonline.mesh.TreeData.TreeType;

public class WurmMapUploader {
	
	// constants to adjust heightmap 
	private static final int ELEVATION_SCALE = 6;
	private static final int ELEVATION_SHIFT = 18500;

	//  some options
	private static final boolean IMPORT_CAVE_IMAGE = true; //set true to generate cave tiles with lots of veins, uniformly distributed 
	private static final boolean TWEAK_TERRAIN = false;// these tweaks were used for initial generation of Treasure Island map and would be useless for 99% of maps. 
	private static final boolean TWEAK_HEIGHT = true;// these tweaks eliminate very deep water, deep marsh and rise underwater clay tiles.  
	private static final boolean REMOVE_EXCESSIVE_TREES = true;// set true to randomly eliminate excessive trees (if terrain map has solid areas of forest)  
	private static int treesDensity = 40; //% of tree tiles that would remain on map

// colors used in terrain map
	public static final int LAVA_COLOR = -2673890;
	public static final int SAND_COLOR = -6253715;
	public static final int CLIFF_COLOR = -6580332;
	public static final int TUNDRA_COLOR = -9009299;
	public static final int STEPPE_COLOR = -9276093;
	public static final int ROCK_COLOR = -9277845;
	public static final int CLAY_COLOR = -9339786;
	public static final int MOSS_COLOR = -9793992;
	public static final int DIRT_COLOR = -11845841;
	public static final int MYCELIUM_COLOR = -12123597;
	public static final int REED_COLOR = -13212353;
	public static final int KELP_COLOR = -13212393;
	public static final int GRASS_COLOR = -13212413;
	public static final int PEAT_COLOR = -13228256;
	public static final int MARSH_COLOR = -13933240;
	public static final int TAR_COLOR = -15592152;
	public static final int SNOW_COLOR = -1;
	public static final int THORN_COLOR = -14075330;
	public static final int ROSE_COLOR = -14075340;
	public static final int OLEANDER_COLOR = -14075350;
	public static final int LAVENDER_COLOR = -14075360;
	public static final int GRAPE_COLOR = -14075370;
	public static final int CAMELLIA_COLOR = -14075380;
	public static final int WILLOW_COLOR = -14075170;
	public static final int WALNUT_COLOR = -14075180;
	public static final int PINE_COLOR = -14075190;
	public static final int OLIVE_COLOR = -14075200;
	public static final int OAK_COLOR = -14075210;
	public static final int MAPLE_COLOR = -14075220;
	public static final int LINDEN_COLOR = -14075230;
	public static final int LEMON_COLOR = -14075240;
	public static final int FIR_COLOR = -14075250;
	public static final int CHESTNUT_COLOR = -14075260;
	public static final int CHERRY_COLOR = -14075270;
	public static final int CEDAR_COLOR = -14075280;
	public static final int BIRCH_COLOR = -14075290;
	public static final int APPLE_COLOR = -14075300;

	// colors used in cave map	
	public static final int CAVE_COLOR = -8421505;
	public static final int CAVE_LAVA_COLOR = -2673890;
	public static final int CAVE_MARBLE_COLOR = -1118482;
	public static final int CAVE_ADAMANTINE_COLOR = -14075270;
	public static final int CAVE_COPPER_COLOR = -12684181;
	public static final int CAVE_GLIMMERSTEEL_COLOR = -3618616;
	public static final int CAVE_GOLD_COLOR = -10240;
	public static final int CAVE_IRON_COLOR = -13754347;
	public static final int CAVE_LEAD_COLOR = -11185074;
	public static final int CAVE_SILVER_COLOR = -5921371;
	public static final int CAVE_TIN_COLOR = -14075380;
	public static final int CAVE_ZINC_COLOR = -9929338;
	public static final int CAVE_SLATE_COLOR = -14803426;

	public static int treeCount = 0;
	public static int bushCount = 0;
	public static int exposedRockCount = 0;
	public static int landTilesCount = 0;

	public static void main(String[] args) throws IOException {
		
		if ((args.length == 2)&&(args[0].equals("create"))) {
			createBlankMap(args[1]);
			System.out.println("Done!!!");
		} else if ((args.length == 2)&&(args[0].equals("dump"))) {
			MapData mapData =  openMap(args[1]);
			dumpMapData(mapData);
		    mapData.close();
			System.out.println("Done!!!");
		} else if ((args.length == 5)&&(args[0].equals("preview"))) {
			MapData mapData =  openMap(args[1]);
			mapData = importMap (mapData, args[2], args[3], args[4]);
			dumpMapData(mapData);
		    mapData.close();
			System.out.println("Done!!!");
		} else if ((args.length == 5)&&(args[0].equals("load"))) {
			MapData mapData =  openMap(args[1]);
			mapData = importMap (mapData, args[2], args[3], args[4]);
			dumpMapData(mapData);
		    mapData.saveChanges();
		    mapData.close();
			System.out.println("Done!!!");
		} else if ((args.length == 1)&&(args[0].equals("help"))) {
			displayInfo();
		} else if (args.length == 0) {
			displayInfo();
		} else {
			System.err.println("Arguments cannot be recognized");
			displayInfo();
		}
		
	}
	
	private static void dumpMapData(MapData mapData) throws IOException {
	    BufferedImage dumpImage = mapData.createMapDump();
	    File outputfile = new File("dump_image.png");
	    ImageIO.write(dumpImage, "png", outputfile);

	    dumpImage = mapData.createTerrainDump(true);
	    outputfile = new File("terrain_dump_image.png");
	    ImageIO.write(dumpImage, "png", outputfile);

	    dumpImage = mapData.createTerrainDump(false);
	    outputfile = new File("terrain_dump_image_nowater.png");
	    ImageIO.write(dumpImage, "png", outputfile);

	    dumpImage = mapData.createTopographicDump(false, (short)100);
	    outputfile = new File("topographic_dump_image.png");
	    ImageIO.write(dumpImage, "png", outputfile);

	    dumpImage = createCaveDump(mapData);
	    outputfile = new File("cave_dump_image.png");
	    ImageIO.write(dumpImage, "png", outputfile);

	}

	private static void displayInfo() {
		System.out.println("Wurm Unlimited map uploader");
		System.out.println("Command syntaxis:");
		System.out.println();
		System.out.println("Create a blank 2048x2048 tiles map in the folder <map folder>.");
		System.out.println("java WurmMapUploader create <map folder>");
		System.out.println();
		System.out.println("Create dump images for height map, terrain map and cave map images without uploading them. NOTE: these dumps for image files, NOT for WU map data in <map folder>. EXISTING DUMP FILES WILL BE OVERWRITTEN. This function will exit with error if images cannot be uploaded.");
		System.out.println("java WurmMapUploader preview <WU map folder> <height map image> <terrain map image> <cave map image>");
		System.out.println();
		System.out.println("Upload height, terrain and cave maps AND create dump images. EXISTING MAP AND DUMP FILES WILL BE OVERWRITTEN. This function will exit with error if images cannot be uploaded.");
		System.out.println("java WurmMapUploader load <WU map folder> <height map image> <terrain map image> <cave map image>");
		System.out.println();
		System.out.println("Generate dump images for Wurm Unlimited map data. EXISTING DUMP FILES WILL BE OVERWRITTEN.");
		System.out.println("java WurmMapUploader dump <WU map folder>");
		System.out.println();
		System.out.println("Important note: height map file must be 16-bit greatscale PNG, terrain map and cave map files must be 24 or 32 bit color PNG");
		System.out.println("only 2048x2048 maps can be loaded in the current version");
		System.out.println("see terrain and cave colors in README file");
		
	}

	private static void createBlankMap(String fileName) {
		try {
			WurmAPI api = WurmAPI.create(fileName,11);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static MapData openMap(String fileName) throws IOException {
		WurmAPI api = WurmAPI.open(fileName);
		MapData data = api.getMapData();
		return data;
	}

	private static MapData importMap(MapData mapData, String heightMapFileName, String terrainMapFileName, String caveMapFileName) throws IOException {
		BufferedImage image = ImageIO.read(new File(heightMapFileName));
		BufferedImage terrainImage = ImageIO.read(new File(terrainMapFileName));
		BufferedImage caveImage = ImageIO.read(new File(caveMapFileName));
		
//		System.out.println(image.getColorModel().toString());
		
		int imageHeight = image.getHeight();
		if (imageHeight != 2048) {
			System.err.println("Image height not equal to 2048");
			System.exit(1);
		}
		int imageWidth = image.getWidth();
		if (imageWidth != imageHeight) {
			System.err.println("Image height not equal 2048");
			System.exit(1);
		}
		int terrainImageHeight = terrainImage.getHeight();
		int terrainImageWidth = terrainImage.getWidth();
		if ((terrainImageHeight != imageHeight)||(terrainImageWidth != imageWidth)){
			System.err.println("Height map and terrain map must have the same size");
			System.exit(1);
		}
		int caveImageHeight = caveImage.getHeight();
		int caveImageWidth = caveImage.getWidth();
		if ((caveImageHeight != imageHeight)||(caveImageWidth != imageWidth)){
			System.err.println("Height map and rock map must have the same size");
			System.exit(1);
		}
			
		Raster raster = image.getRaster();
		
	    for (int x = 0; x < imageWidth; x++)
	    {
	        for (int y = 0; y < imageHeight; y++)
	        {
	        	int[] pixelColor = new int[2];
	        	Color color = new Color(terrainImage.getRGB(x,y));
	        	Tile tileType = decodeColor (color); 
	        	raster.getPixel(x, y, pixelColor);
	        	short height = (short) ((pixelColor[0] - Short.MAX_VALUE + ELEVATION_SHIFT)/ELEVATION_SCALE);
	        	if (height > 0) landTilesCount++;

	        	if (TWEAK_HEIGHT) {
	        		height = tweakHeight (x, y, color, height);
	        	}
	        	if (TWEAK_TERRAIN){
	        		tileType = tweakTerrain (x, y, tileType, height);
	        	}
	        	
	        	if (tileType.isTree()) {
    				if (REMOVE_EXCESSIVE_TREES) {
	        			Random rand = new Random();
	        			int randomNumber = rand.nextInt(100); 
	        			if (randomNumber < treesDensity) { //80% of trees filtered out
	        					if ((!mapData.getSurfaceTile(x-1, y).isTree())&&(!mapData.getSurfaceTile(x, y-1).isTree())){
	        						treeCount++;
	        						mapData.setSurfaceTile(x, y, Tile.TILE_GRASS, height);
	        						mapData.setTree(x, y, decodeTree(color), FoliageAge.YOUNG_THREE, GrassData.GrowthTreeStage.MEDIUM);
	        					} else {
	        						mapData.setSurfaceTile(x, y, Tile.TILE_GRASS, height);
	        					}
	        			} else {
	        				mapData.setSurfaceTile(x, y, Tile.TILE_GRASS, height);
	        			}
    				} else {
    					treeCount++;
    					mapData.setTree(x, y, decodeTree(color), FoliageAge.YOUNG_THREE, GrassData.GrowthTreeStage.MEDIUM);
    				}
	        	} else if (tileType.isBush()){
	        		bushCount++;
	        		mapData.setSurfaceTile(x, y, Tile.TILE_GRASS, height);
	        		mapData.setBush(x, y, decodeBush(color), FoliageAge.YOUNG_THREE, GrassData.GrowthTreeStage.MEDIUM);
	        	} else {
	        		mapData.setSurfaceTile(x, y, tileType, height);
	        	}
	        	
	        	short rockHeight = calculateRockHeight (color, height);
        		mapData.setRockHeight(x, y, rockHeight);
        		Tile caveTile; 
	        	if (IMPORT_CAVE_IMAGE) {
	        		Color caveColor = new Color(caveImage.getRGB(x, y));
	        		//System.out.println("x " + x + " y " + y + " RGB" + caveColor.getRGB());
	        		caveTile = decodeCaveColor(caveColor);
	        	} else {
	        		caveTile = generateCaveTile(x*imageHeight + y, rockHeight);
	        	}
	        	short resourceCount = 51;
        		if (caveTile == Tile.TILE_CAVE_WALL) {
        			mapData.setCaveTile(x, y, generateCaveTile(x*imageHeight + y, rockHeight), resourceCount);
        		} else {
        			Random rand = new Random();
        			resourceCount = (short) (rand.nextInt(10000) + 51);
        			mapData.setCaveTile(x, y, generateCaveTile(x*imageHeight + y, rockHeight), resourceCount);
        		}
	        }
	        
	    }
	    System.out.println("Total number of land tiles is " + landTilesCount);
	    System.out.println("Total number of rock tiles is " + exposedRockCount);
	    System.out.println("Total number of trees is " + treeCount);
	    System.out.println("Total number of bushes is " + bushCount);

	    return mapData;
	}
	
	private static Color encodeCaveTile(Tile tile) {
		Color color = new Color (-1);
		if (tile == Tile.TILE_CAVE_WALL) {
			color = new Color(CAVE_COLOR);
		} else if (tile == Tile.TILE_CAVE_WALL_ORE_ADAMANTINE) {
			color = new Color(CAVE_ADAMANTINE_COLOR);
		} else if (tile == Tile.TILE_CAVE_WALL_ORE_COPPER) {
			color = new Color(CAVE_COPPER_COLOR);
		} else if (tile == Tile.TILE_CAVE_WALL_ORE_GLIMMERSTEEL) {
			color = new Color(CAVE_GLIMMERSTEEL_COLOR);
		} else if (tile == Tile.TILE_CAVE_WALL_ORE_GOLD) {
			color = new Color(CAVE_GOLD_COLOR);
		} else if (tile == Tile.TILE_CAVE_WALL_ORE_IRON) {
			color = new Color(CAVE_IRON_COLOR);
		} else if (tile == Tile.TILE_CAVE_WALL_LAVA) {
			color = new Color(CAVE_LAVA_COLOR);
		} else if (tile == Tile.TILE_CAVE_WALL_ORE_LEAD) {
			color = new Color(CAVE_LEAD_COLOR);
		} else if (tile == Tile.TILE_CAVE_WALL_MARBLE) {
			color = new Color(CAVE_MARBLE_COLOR);
		} else if (tile == Tile.TILE_CAVE_WALL_ORE_SILVER) {
			color = new Color(CAVE_SILVER_COLOR);
		} else if (tile == Tile.TILE_CAVE_WALL_SLATE) {
			color = new Color(CAVE_SLATE_COLOR);
		} else if (tile == Tile.TILE_CAVE_WALL_ORE_TIN) {
			color = new Color(CAVE_TIN_COLOR);
		} else if (tile == Tile.TILE_CAVE_WALL_ORE_ZINC) {
			color = new Color(CAVE_ZINC_COLOR);
		} else {
			System.err.println("Cave tile type not supported: " + tile.tilename + ". This tile will have white color on cave dump image.");
			color = new Color(255, 255, 255);
		}
		return color;
	}

	private static Tile generateCaveTile(int tileNumber, short height) {
		if (height > -15) { //no veins on tiles which are too deep for surface mining 
			if (tileNumber%37 == 0) {
			return Tile.TILE_CAVE_WALL_ORE_IRON;
			} else if (tileNumber%43 == 0) {
				return Tile.TILE_CAVE_WALL_SLATE;
			} else if (tileNumber%47 == 0) {
				return Tile.TILE_CAVE_WALL_MARBLE;
			} else if (tileNumber%53 == 0) {
				return Tile.TILE_CAVE_WALL_ORE_COPPER;
			} else if (tileNumber%59 == 0) {
				return Tile.TILE_CAVE_WALL_ORE_ZINC;
			} else if (tileNumber%61 == 0) {
				return Tile.TILE_CAVE_WALL_ORE_TIN;
			} else if (tileNumber%67 == 0) {
				return Tile.TILE_CAVE_WALL_ORE_LEAD;
			} else if (tileNumber%71 == 0) {
				return Tile.TILE_CAVE_WALL_ORE_SILVER;
			} else if (tileNumber%73 == 0) {
				return Tile.TILE_CAVE_WALL_ORE_GOLD;
			} else if (tileNumber%79 == 0) {
				return Tile.TILE_CAVE_WALL_ORE_ADAMANTINE;
			} else if (tileNumber%83 == 0) {
				return Tile.TILE_CAVE_WALL_ORE_GLIMMERSTEEL;
			} else {
				return Tile.TILE_CAVE_WALL;
			}
		} else {
			return Tile.TILE_CAVE_WALL;
		}
	}

	private static Tile decodeCaveColor(Color color) {
		if (color.getRGB() == CAVE_ADAMANTINE_COLOR) {
			return Tile.TILE_CAVE_WALL_ORE_ADAMANTINE;
		} else if (color.getRGB() == CAVE_COLOR) {
			return Tile.TILE_CAVE_WALL;
		} else if (color.getRGB() == CAVE_COPPER_COLOR) {
			return Tile.TILE_CAVE_WALL_ORE_COPPER;
		} else if (color.getRGB() == CAVE_GLIMMERSTEEL_COLOR) {
			return Tile.TILE_CAVE_WALL_ORE_GLIMMERSTEEL;
		} else if (color.getRGB() == CAVE_GOLD_COLOR) {
			return Tile.TILE_CAVE_WALL_ORE_GOLD;
		} else if (color.getRGB() == CAVE_IRON_COLOR) {
			return Tile.TILE_CAVE_WALL_ORE_IRON;
		} else if (color.getRGB() == CAVE_LAVA_COLOR) {
			return Tile.TILE_CAVE_WALL_LAVA;
		} else if (color.getRGB() == CAVE_LEAD_COLOR) {
			return Tile.TILE_CAVE_WALL_ORE_LEAD;
		} else if (color.getRGB() == CAVE_MARBLE_COLOR) {
			return Tile.TILE_CAVE_WALL_MARBLE;
		} else if (color.getRGB() == CAVE_SILVER_COLOR) {
			return Tile.TILE_CAVE_WALL_ORE_SILVER;
		} else if (color.getRGB() == CAVE_SLATE_COLOR) {
			return Tile.TILE_CAVE_WALL_SLATE;
		} else if (color.getRGB() == CAVE_TIN_COLOR){
			return Tile.TILE_CAVE_WALL_ORE_TIN;
		} else if (color.getRGB() == CAVE_ZINC_COLOR){
			return Tile.TILE_CAVE_WALL_ORE_ZINC;
		} else {
			System.err.println("Unknown RGB color for cave wall: Red = " + color.getRed() + " Green = " + color.getGreen() + " Blue = " + color.getBlue());
			System.exit(1);
		}
		return null;
	}

	private static BushType decodeBush(Color color) {
	if (color.getRGB() == CAMELLIA_COLOR) {
		return BushType.CAMELLIA;
	} else if (color.getRGB() == GRAPE_COLOR) {
		return BushType.GRAPE;
	} else if (color.getRGB() == LAVENDER_COLOR) {
		return BushType.LAVENDER;
	} else if (color.getRGB() == OLEANDER_COLOR) {
		return BushType.OLEANDER;
	} else if (color.getRGB() == ROSE_COLOR) {
		return BushType.ROSE;
	} else if (color.getRGB() == THORN_COLOR) {
		return BushType.THORN;
	} else {
		System.err.println("Unknown RGB color for bush " + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
		System.exit(1);
	}
	return null;
	}
	
	private static TreeType decodeTree(Color color) {
		if (color.getRGB() == APPLE_COLOR) {
			return TreeType.APPLE;
		} else if (color.getRGB() == BIRCH_COLOR) {
			return TreeType.BIRCH;
		} else if (color.getRGB() == CEDAR_COLOR) {
			return TreeType.CEDAR;
		} else if (color.getRGB() == CHERRY_COLOR) {
			return TreeType.CHERRY;
		} else if (color.getRGB() == CHESTNUT_COLOR) {
			return TreeType.CHESTNUT;
		} else if (color.getRGB() == FIR_COLOR) {
			return TreeType.FIR;
		} else if (color.getRGB() == LEMON_COLOR) {
			return TreeType.LEMON;
		} else if (color.getRGB() == LINDEN_COLOR) {
			return TreeType.LINDEN;
		} else if (color.getRGB() == MAPLE_COLOR) {
			return TreeType.MAPLE;
		} else if (color.getRGB() == OAK_COLOR) {
			return TreeType.OAK;
		} else if (color.getRGB() == OLIVE_COLOR) {
			return TreeType.OLIVE;
		} else if (color.getRGB() == PINE_COLOR) {
			return TreeType.PINE;
		} else if (color.getRGB() == WALNUT_COLOR) {
			return TreeType.WALNUT;
		} else if (color.getRGB() == WILLOW_COLOR) {
			return TreeType.WILLOW;
		} else {
			System.err.println("Unknown RGB color for tree " + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
			System.exit(1);
		}
		return null;
	}

	private static Tile decodeColor (Color color) {
		if (color.getRGB() == SNOW_COLOR) {
			return Tile.TILE_SNOW;
		} else if (color.getRGB() == TAR_COLOR) {
			return Tile.TILE_TAR;
		} else if (color.getRGB() == CAMELLIA_COLOR) {
			return Tile.TILE_BUSH_CAMELLIA;
		} else if (color.getRGB() == GRAPE_COLOR) {
			return Tile.TILE_BUSH_GRAPE;
		} else if (color.getRGB() == LAVENDER_COLOR) {
			return Tile.TILE_BUSH_LAVENDER;
		} else if (color.getRGB() == OLEANDER_COLOR) {
			return Tile.TILE_BUSH_OLEANDER;
		} else if (color.getRGB() == ROSE_COLOR) {
			return Tile.TILE_BUSH_ROSE;
		} else if (color.getRGB() == THORN_COLOR) {
			return Tile.TILE_BUSH_THORN;
		} else if (color.getRGB() == APPLE_COLOR) {
			return Tile.TILE_TREE_APPLE;
		} else if (color.getRGB() == BIRCH_COLOR) {
			return Tile.TILE_TREE_BIRCH;
		} else if (color.getRGB() == CEDAR_COLOR) {
			return Tile.TILE_TREE_CEDAR;
		} else if (color.getRGB() == CHERRY_COLOR) {
			return Tile.TILE_TREE_CHERRY;
		} else if (color.getRGB() == CHESTNUT_COLOR) {
			return Tile.TILE_TREE_CHESTNUT;
		} else if (color.getRGB() == FIR_COLOR) {
			return Tile.TILE_TREE_FIR;
		} else if (color.getRGB() == LEMON_COLOR) {
			return Tile.TILE_TREE_LEMON;
		} else if (color.getRGB() == LINDEN_COLOR) {
			return Tile.TILE_TREE_LINDEN;
		} else if (color.getRGB() == MAPLE_COLOR) {
			return Tile.TILE_TREE_MAPLE;
		} else if (color.getRGB() == OAK_COLOR) {
			return Tile.TILE_TREE_OAK;
		} else if (color.getRGB() == OLIVE_COLOR) {
			return Tile.TILE_TREE_OLIVE;
		} else if (color.getRGB() == PINE_COLOR) {
			return Tile.TILE_TREE_PINE;
		} else if (color.getRGB() == WALNUT_COLOR) {
			return Tile.TILE_TREE_WALNUT;
		} else if (color.getRGB() == WILLOW_COLOR) {
			return Tile.TILE_TREE_WILLOW;
		} else if (color.getRGB() == MARSH_COLOR) {
			return Tile.TILE_MARSH;
		} else if (color.getRGB() == PEAT_COLOR) {
			return Tile.TILE_PEAT;
		} else if (color.getRGB() == GRASS_COLOR) {
			return Tile.TILE_GRASS;
		} else if (color.getRGB() == KELP_COLOR) {
			return Tile.TILE_KELP;
		} else if (color.getRGB() == REED_COLOR) {
			return Tile.TILE_REED;
		} else if (color.getRGB() == MYCELIUM_COLOR) {
			return Tile.TILE_MYCELIUM;
		} else if (color.getRGB() == DIRT_COLOR) {
			return Tile.TILE_DIRT;
		} else if (color.getRGB() == MOSS_COLOR) {
			return Tile.TILE_MOSS;
		} else if (color.getRGB() == CLAY_COLOR) {
			return Tile.TILE_CLAY;
		} else if (color.getRGB() == ROCK_COLOR) {
			return Tile.TILE_ROCK;
		} else if (color.getRGB() == STEPPE_COLOR) {
			return Tile.TILE_STEPPE;
		} else if (color.getRGB() == TUNDRA_COLOR) {
			return Tile.TILE_TUNDRA;
		} else if (color.getRGB() == CLIFF_COLOR) {
			return Tile.TILE_CLIFF;
		} else if (color.getRGB() == SAND_COLOR) {
			return Tile.TILE_SAND;
		} else if (color.getRGB() == LAVA_COLOR) {
			return Tile.TILE_LAVA;
		} else {
			System.err.println("Unknown RGB color " + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
			System.exit(1);
		}
		return null;
	}
	
	private static short calculateRockHeight(Color color, short height) {
		short returnVal = Short.MIN_VALUE;
		if (color.getRGB() == ROCK_COLOR){
			exposedRockCount++;
			returnVal = height; //expose rock layer
		} else if (color.getRGB() == CLIFF_COLOR) {
			returnVal = (short) (height-1); //cliffs have rock right under surface
		} else if (height < -100) {
			returnVal = (short) (height - 30); //deep water has a thin layer of dirt over rock
		} else if (color.getRGB() == MARSH_COLOR) {
			returnVal = (short) (height - 25); //marsh 
		} else if (height < 0) {
			returnVal = (short) (height *1.1 -20);//shallow water: some dirt for dredging
		} else if (height < 1000) {
			returnVal = (short) (height*0.9 - 20); //lowlands have thick dirt layer, from 20 dirt at water level to 120 dirt at 999  
		} else if (height < 3000) {
			returnVal = (short) (height*0.9 + 80); //highlands have variable dirt layer, from 20 dirt at 1000 to 220 dirt at 3000.  
		} else {
			returnVal = (short) (height - 20); // peaks over 3000 height have thin dirt layer 
		}
		return returnVal;
	}

	private static BufferedImage createCaveDump(MapData mapData) {
        int lWidth = 16384;
        if (lWidth > mapData.getWidth())
            lWidth = mapData.getWidth();
        int yo = mapData.getWidth() - lWidth;
        if (yo < 0)
            yo = 0;
        int xo = mapData.getWidth() - lWidth;
        if (xo < 0)
            xo = 0;

        final Random random = new Random();
        if (xo > 0)
            xo = random.nextInt(xo);
        if (yo > 0)
            yo = random.nextInt(yo);

        final BufferedImage bi2 = new BufferedImage(lWidth, lWidth, BufferedImage.TYPE_INT_RGB);
        final float[] data = new float[lWidth * lWidth * 3];
        
        for (int x = 0; x < lWidth; x++) {
            for (int y = lWidth - 1; y >= 0; y--) {
                final Tile tile = mapData.getCaveTile(x + xo, y + yo);

                final Color color;
                if (tile != null) {
                    color = encodeCaveTile(tile);
                }
                else {
                	System.out.println("Cave on tile " + (x + xo) + ", " + (y + yo) + " is not set. It will have white color on cave dump image");
                    color = new Color (255,255,255);
                }
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                data[(x + y * lWidth) * 3 + 0] = r;
                data[(x + y * lWidth) * 3 + 1] = g;
                data[(x + y * lWidth) * 3 + 2] = b;
            }
        }

        bi2.getRaster().setPixels(0, 0, lWidth, lWidth, data);
        return bi2;
	}

	private static short tweakHeight(int x, int y, Color color, short height) {
		if (height < -100) {
			height = (short)(height/10 -100); //no need to have very deep water
		}
		if ((color.getRGB() == MARSH_COLOR)&&(height<-20)) {
			height = -20; // get rid of deep water swamps
		} else if ((color.getRGB() == CLAY_COLOR)&&(height<1)) {
			height = 1; // rise clay
		}
		return height;
	}
	private static Tile tweakTerrain(int x, int y, Tile tileType, short height) {
    	if (height < 0) {
			if ((x > 1000)&&(x<1400)&&(y>1000)&&(y<1490)) {
				if ((height < -1)&&(height > -9)) {
					return Tile.TILE_REED;
				} else {
					return Tile.TILE_MARSH;
				}
			} else {
    			if (height < -90) {
    				return Tile.TILE_DIRT;
    			} else if (height == -15) {
    				return Tile.TILE_KELP;
    			} else {
    				return Tile.TILE_SAND;
    			}
   			}
    	} else if (height < 30) {
    		return Tile.TILE_SAND;
    	} else if (height < 2000) {
    		return Tile.TILE_GRASS;
		} else if (height < 3000) {
			return Tile.TILE_STEPPE;
		} else {
			return Tile.TILE_ROCK;
		}
	}


}
