package com.wurmonline.wurmapi.api;

import java.awt.Color;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.wurmonline.mesh.BushData.BushType;
import com.wurmonline.mesh.FoliageAge;
import com.wurmonline.mesh.GrassData.FlowerType;
import com.wurmonline.mesh.GrassData.GrowthStage;
import com.wurmonline.mesh.GrassData.GrowthTreeStage;
import com.wurmonline.mesh.Tiles.Tile;
import com.wurmonline.mesh.TreeData.TreeType;

public class WurmMapUploader {

	private static final boolean RANDOMIZE_TREE_AGE = true; 
	// if false, all trees will have age YOUNG_THREE
	
	private static final boolean RANDOMIZE_BUSH_AGE = true; 
	// if false, all bushes will have age YOUNG_THREE
	
	private static final boolean RANDOMIZE_GRASS_GROWTH_STAGE = true; 
	// if false, all grass tiles will have growth stage MEDIUM
	
	private static final boolean RANDOMIZE_FLOWER_TYPES = true; 
	// if true, random flower types will be set on grass tiles. Color codes for flowers will be ignored, each grass tile can have or have no flowers on it.   
  
	private static int FLOWERS_DENSITY = 10; // % of grass tiles with flowers
	
	// constants to adjust height map and rock height map
	// ELEVATION_SHIFT shifts every tile up by the specified number of units
	// ELEVATION_SCALE scales down elevations (and depths) from sea level. For
	// example ELEVATION_SCALE=2 will make the whole island two times lower
	// ROCK_HEIGHT_SHIFT and ROCK_HEIGHT_SCALE affect the rock layer in the same
	// way
	private static final int ELEVATION_SCALE = 1;
	private static final int ELEVATION_SHIFT = 0;
	private static final int ROCK_HEIGHT_SCALE = 1;
	private static final int ROCK_HEIGHT_SHIFT = 0;

	// colors used in terrain map
	public static final int SAND_COLOR = -6253715;
	public static final int LAVA_COLOR = -2673890;
	public static final int CLIFF_COLOR = -6580332;
	public static final int TUNDRA_COLOR = -9009299;
	public static final int STEPPE_COLOR = -9276093;
	public static final int ROCK_COLOR = -9277845;
	public static final int CLAY_COLOR = -9339786;
	public static final int MOSS_COLOR = -9793992;
	public static final int DIRT_COLOR = -11845841;
	public static final int MYCELIUM_COLOR = -12123597;
	public static final int REED_COLOR = -13212353;
	public static final int KELP_COLOR = -10242775;
	public static final int GRASS_COLOR = -13212413;
	public static final int PEAT_COLOR = -13228256;
	public static final int MARSH_COLOR = -13933240;
	public static final int TAR_COLOR = -15592152;
	public static final int SNOW_COLOR = -1;
	public static final int THORN_COLOR = -7851476;
	public static final int ROSE_COLOR = -54478;
	public static final int OLEANDER_COLOR = -408082;
	public static final int LAVENDER_COLOR = -2735361;
	public static final int GRAPE_COLOR = -6734181;
	public static final int CAMELLIA_COLOR = -394849;
	public static final int WILLOW_COLOR = -8797844;
	public static final int WALNUT_COLOR = -8332527;
	public static final int PINE_COLOR = -4568296;
	public static final int OLIVE_COLOR = -7298962;
	public static final int OAK_COLOR = -8665599;
	public static final int MAPLE_COLOR = -4768768;
	public static final int LINDEN_COLOR = -5521063;
	public static final int LEMON_COLOR = -5794;
	public static final int FIR_COLOR = -10826094;
	public static final int CHESTNUT_COLOR = -16723102;
	public static final int CHERRY_COLOR = -1957376;
	public static final int CEDAR_COLOR = -11608786;
	public static final int BIRCH_COLOR = -531136;
	public static final int APPLE_COLOR = -16896;

	// colors for different types of flowers on grass tiles
	public static final int GRASS_FLOWER1_COLOR = -394839;
	public static final int GRASS_FLOWER2_COLOR = -394834;
	public static final int GRASS_FLOWER3_COLOR = -394829;
	public static final int GRASS_FLOWER4_COLOR = -394824;
	public static final int GRASS_FLOWER5_COLOR = -394819;
	public static final int GRASS_FLOWER6_COLOR = -394814;
	public static final int GRASS_FLOWER7_COLOR = -394809;
	public static final int GRASS_FLOWER8_COLOR = -394804;
	public static final int GRASS_FLOWER9_COLOR = -394799;
	public static final int GRASS_FLOWER10_COLOR = -394794;
	public static final int GRASS_FLOWER11_COLOR = -394789;
	public static final int GRASS_FLOWER12_COLOR = -394784;
	public static final int GRASS_FLOWER13_COLOR = -394779;
	public static final int GRASS_FLOWER14_COLOR = -394774;
	public static final int GRASS_FLOWER15_COLOR = -394769;

	// colors used in cave map and cave dump
	public static final int CAVE_WALL_COLOR = -8421505;
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
	public static final int CAVE_WALL_REINFORCED_COLOR = -12566464;

	// colors used in cave dump only
	public static final int CAVE_FLOOR_REINFORCED_COLOR = -4605511;
	public static final int CAVE_EXIT_COLOR = -16777216;
	public static final int CAVE_COLOR = -1;
	public static final int NOT_CAVE_TILE_COLOR = -32887;
	public static final int UNKNOWN_CAVE_TILE_COLOR = -65536;

	private final MapData mapData;
	private static int mapSize;
	private final Random rand = new Random();

	// counters
	public static int treeCount = 0;
	public static int bushCount = 0;
	public static int exposedRockCount = 0;
	public static int landTilesCount = 0;

	WurmMapUploader(String dir) throws IOException {
		WurmAPI api = WurmAPI.open(dir);
		this.mapData = api.getMapData();
	}

	public static void main(String[] args) throws IOException {

		if ((args.length == 3) && (args[0].equals("create"))) {
			createBlankMap(args[1], args[2]);
			System.out.println("Done!!!");
		} else if ((args.length == 2) && (args[0].equals("dump"))) {
			WurmMapUploader uploader = new WurmMapUploader(args[1]);
			uploader.dumpMapData();
			uploader.close();
			System.out.println("Done!!!");
		} else if ((args.length == 6) && (args[0].equals("preview"))) {
			WurmMapUploader uploader = new WurmMapUploader(args[1]);
			uploader.importMap(args[2], args[3], args[4], args[5]);
			uploader.dumpMapData();
			uploader.close();
			System.out.println("Done!!!");
		} else if ((args.length == 6) && (args[0].equals("load"))) {
			WurmMapUploader uploader = new WurmMapUploader(args[1]);
			uploader.importMap(args[2], args[3], args[4], args[5]);
			uploader.dumpMapData();
			uploader.saveChanges();
			uploader.close();
			System.out.println("Done!!!");
		} else if ((args.length == 2) && (args[0].equals("export"))) {
			WurmMapUploader uploader = new WurmMapUploader(args[1]);
			uploader.exportMaps();
			uploader.close();
			System.out.println("Done!!!");
		} else if ((args.length == 1) && (args[0].equals("help"))) {
			displayInfo();
		} else if (args.length == 0) {
			displayInfo();
		} else {
			System.err.println("Arguments cannot be recognized");
			displayInfo();
		}

	}

	private void saveChanges() {
		this.mapData.saveChanges();
	}

	private void close() {
		this.mapData.close();
	}

	public void dumpMapData() throws IOException {
		BufferedImage dumpImage = this.mapData.createMapDump();
		File outputfile = new File("dump_image.png");
		ImageIO.write(dumpImage, "png", outputfile);

		dumpImage = mapData.createTerrainDump(true);
		outputfile = new File("terrain_dump_image.png");
		ImageIO.write(dumpImage, "png", outputfile);

		dumpImage = mapData.createTerrainDump(false);
		outputfile = new File("terrain_dump_image_nowater.png");
		ImageIO.write(dumpImage, "png", outputfile);

		dumpImage = mapData.createTopographicDump(false, (short) 100);
		outputfile = new File("topographic_dump_image.png");
		ImageIO.write(dumpImage, "png", outputfile);

		dumpImage = createRockTopographicDump(mapData, false, (short) 100);
		outputfile = new File("rock_dump_image.png");
		ImageIO.write(dumpImage, "png", outputfile);

		dumpImage = createCaveDump(mapData);
		outputfile = new File("cave_dump_image.png");
		ImageIO.write(dumpImage, "png", outputfile);

	}

	public void exportMaps() throws IOException {

		BufferedImage mapImage = this.mapData.createMapDump();// I don't
																// understand
																// why, but this
																// function is
																// required for
																// correct
																// identification
																// of tile
																// properties

		mapImage = exportRockMap(this.mapData);
		File outputfile = new File("exported_rock_map.png");
		ImageIO.write(mapImage, "png", outputfile);

		mapImage = exportHeightMap(this.mapData);
		outputfile = new File("exported_height_map.png");
		ImageIO.write(mapImage, "png", outputfile);

		mapImage = exportCaveMap(this.mapData);
		outputfile = new File("exported_cave_map.png");
		ImageIO.write(mapImage, "png", outputfile);

		mapImage = exportTerrainMap(this.mapData);
		outputfile = new File("exported_terrain_map.png");
		ImageIO.write(mapImage, "png", outputfile);
	}

	private static void displayInfo() {
		System.out.println("Wurm Unlimited map uploader");
		System.out.println("Command syntaxis:");
		System.out.println();
		System.out
				.println("Create a blank map in the folder <map folder>. Supported map sizes are 1024, 2048, 4096");
		System.out.println("java WurmMapUploader create <map folder> <map_size>");
		System.out.println();
		System.out
				.println("Create dump images for height map, terrain map, cave map and rock height map images without uploading them. NOTE: these dumps for image files, NOT for WU map data in <map folder>. EXISTING DUMP FILES WILL BE OVERWRITTEN. This function will exit with error if images cannot be uploaded.");
		System.out
				.println("java WurmMapUploader preview <WU map folder> <height map image> <terrain map image> <cave map image> <rock map image>");
		System.out.println();
		System.out
				.println("Upload height, terrain, cave , rock height maps AND create dump images. EXISTING MAP AND DUMP FILES WILL BE OVERWRITTEN. This function will exit with error if images cannot be uploaded.");
		System.out
				.println("java WurmMapUploader load <WU map folder> <height map image> <terrain map image> <cave map image> <rock map image>");
		System.out.println();
		System.out
				.println("Generate dump images for Wurm Unlimited map data. EXISTING DUMP FILES WILL BE OVERWRITTEN.");
		System.out.println("java WurmMapUploader dump <WU map folder>");
		System.out.println();
		System.out
				.println("Export surface height map and rock layer height map as 16 bit grayscale PNG, terrain and cave maps as color PNG");
		System.out.println("java WurmMapUploader export <WU map folder>");
		System.out.println();
		System.out
				.println("Important notes: height map and rock height map files must be 16-bit greatscale PNG, terrain map and cave map files must be 24 or 32 bit color PNG");
		System.out.println("Only square images are supported. See RGB values of terrain and cave colors in the README.md file.");
	}

	private static void createBlankMap(String fileName, String mapSizeString) {
		try {
			mapSize = Integer.parseInt(mapSizeString);
			WurmAPI api = WurmAPI.create(fileName, getPowTwo(mapSize));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static int getPowTwo (int mapSize) {
		if (mapSize == 1024) {
			return 10;
		} else if (mapSize == 2048) {
			return 11;
		} else if (mapSize == 4096) {
			return 12;
		} else {
			System.err.println("Supported map sizes are 1024, 2048, 4096");
			System.exit(1);
		}
		return 0;
	}

	/*
	 * private static MapData openMap(String fileName) throws IOException {
	 * WurmAPI api = WurmAPI.open(fileName); MapData data = api.getMapData();
	 * return data; }
	 */
	private MapData importMap(String heightMapFileName,
			String terrainMapFileName, String caveMapFileName,
			String rockMapFileName) throws IOException {
		BufferedImage heightImage = ImageIO.read(new File(heightMapFileName));
		BufferedImage terrainImage = ImageIO.read(new File(terrainMapFileName));
		BufferedImage caveImage = ImageIO.read(new File(caveMapFileName));
		BufferedImage rockImage = ImageIO.read(new File(rockMapFileName));

		mapSize = this.mapData.getHeight();
		if (mapSize != this.mapData.getWidth()) {
			System.err.println("Only square maps are supported");
			System.exit(1);
		}

		if (heightImage.getHeight() != mapSize || heightImage.getWidth() != mapSize) {
			System.err.println("Height map dimensions must be equal to Wurm map dimensions");
			System.exit(1);
		}

		if (terrainImage.getHeight() != mapSize || terrainImage.getWidth() != mapSize) {
			System.err
					.println("Terrain map dimensions must be equal to Wurm map dimensions");
			System.exit(1);
		}
		if (caveImage.getHeight() != mapSize || caveImage.getWidth() != mapSize) {
			System.err
					.println("Cave map dimensions must be equal to Wurm map dimensions");
			System.exit(1);
		}
		if (caveImage.getHeight() != mapSize || caveImage.getWidth() != mapSize ) {
			System.err
					.println("Rock height map dimensions must be equal to Wurm map dimensions");
			System.exit(1);
		}

		Raster heightRaster = heightImage.getRaster();
		Raster rockRaster = rockImage.getRaster();

		for (int x = 0; x < mapSize; x++) {
			for (int y = 0; y < mapSize; y++) {
				int[] heightPixelColor = new int[2];

				heightRaster.getPixel(x, y, heightPixelColor);
				short height = (short) ((heightPixelColor[0] - Short.MAX_VALUE + ELEVATION_SHIFT) / ELEVATION_SCALE);
				if (height > 0)
					landTilesCount++;

				Color color = new Color(terrainImage.getRGB(x, y));
				Tile tileType = decodeColor(color);
				if (tileType == Tile.TILE_ROCK)
					exposedRockCount++;

				// set plants and tile types
				if (tileType.isTree()) {
					treeCount++;
					this.mapData.setTree(x, y, decodeTree(color), getTreeAge(),
							getGrassGrowthTreeStage());
				} else if (tileType.isBush()) {
					bushCount++;
					this.mapData.setSurfaceTile(x, y, Tile.TILE_GRASS, height);
					this.mapData.setBush(x, y, decodeBush(color), getBushAge(),
							getGrassGrowthTreeStage());
				} else if (tileType == Tile.TILE_GRASS) {
					this.mapData.setSurfaceTile(x, y, Tile.TILE_GRASS, height);
					if (RANDOMIZE_FLOWER_TYPES) {
						this.mapData.setGrass(x, y, getGrassGrowthStage(),
								getRandomFlower());
					} else {
						this.mapData.setGrass(x, y, getGrassGrowthStage(),
								getFlower(color));
					}
				} else if (tileType == Tile.TILE_KELP) {
					this.mapData.setSurfaceTile(x, y, Tile.TILE_KELP, height);
					this.mapData.setGrass(x, y, getGrassGrowthStage(), null);
				} else if (tileType == Tile.TILE_REED) {
					this.mapData.setSurfaceTile(x, y, Tile.TILE_REED, height);
					this.mapData.setGrass(x, y, getGrassGrowthStage(), null);
				} else {
					this.mapData.setSurfaceTile(x, y, tileType, height);
				}

				// set rock layer
				short rockHeight = 0;
				int[] rockPixelColor = new int[2];
				rockRaster.getPixel(x, y, rockPixelColor);
				rockHeight = (short) ((rockPixelColor[0] - Short.MAX_VALUE + ROCK_HEIGHT_SHIFT) / ROCK_HEIGHT_SCALE);
				mapData.setRockHeight(x, y, rockHeight);

				// set cave tiles
				Tile caveTile;
				Color caveColor = new Color(caveImage.getRGB(x, y));
				// System.out.println("x " + x + " y " + y + " RGB" +
				// caveColor.getRGB());
				caveTile = decodeCaveColor(caveColor);

				// set number of resources
				short resourceCount = 51;
				if (caveTile == Tile.TILE_CAVE_WALL) {
					mapData.setCaveTile(x, y,
							generateCaveTile(x * mapSize + y, rockHeight),
							resourceCount);
				} else {
					Random rand = new Random();
					resourceCount = (short) (rand.nextInt(10000) + 51);
					mapData.setCaveTile(x, y,
							generateCaveTile(x * mapSize + y, rockHeight),
							resourceCount);
				}
			}
		}
		System.out.println("Total number of land tiles is " + landTilesCount);
		System.out.println("Total number of rock tiles is " + exposedRockCount);
		System.out.println("Total number of trees is " + treeCount);
		System.out.println("Total number of bushes is " + bushCount);

		return mapData;
	}

	private FlowerType getFlower(Color color) {
		if (color.getRGB() == GRASS_FLOWER1_COLOR) {
			return FlowerType.FLOWER_1;
		} else if (color.getRGB() == GRASS_FLOWER2_COLOR) {
			return FlowerType.FLOWER_2;
		} else if (color.getRGB() == GRASS_FLOWER3_COLOR) {
			return FlowerType.FLOWER_3;
		} else if (color.getRGB() == GRASS_FLOWER4_COLOR) {
			return FlowerType.FLOWER_4;
		} else if (color.getRGB() == GRASS_FLOWER5_COLOR) {
			return FlowerType.FLOWER_5;
		} else if (color.getRGB() == GRASS_FLOWER6_COLOR) {
			return FlowerType.FLOWER_6;
		} else if (color.getRGB() == GRASS_FLOWER7_COLOR) {
			return FlowerType.FLOWER_7;
		} else if (color.getRGB() == GRASS_FLOWER8_COLOR) {
			return FlowerType.FLOWER_8;
		} else if (color.getRGB() == GRASS_FLOWER9_COLOR) {
			return FlowerType.FLOWER_9;
		} else if (color.getRGB() == GRASS_FLOWER10_COLOR) {
			return FlowerType.FLOWER_10;
		} else if (color.getRGB() == GRASS_FLOWER11_COLOR) {
			return FlowerType.FLOWER_11;
		} else if (color.getRGB() == GRASS_FLOWER12_COLOR) {
			return FlowerType.FLOWER_12;
		} else if (color.getRGB() == GRASS_FLOWER13_COLOR) {
			return FlowerType.FLOWER_13;
		} else if (color.getRGB() == GRASS_FLOWER14_COLOR) {
			return FlowerType.FLOWER_14;
		} else if (color.getRGB() == GRASS_FLOWER15_COLOR) {
			return FlowerType.FLOWER_15;
		} else {
			return FlowerType.NONE;
		}
	}

	private GrowthTreeStage getGrassGrowthTreeStage() {
		if (RANDOMIZE_GRASS_GROWTH_STAGE) {
			int age = rand.nextInt(4);
			if (age == 0) {
				return GrowthTreeStage.SHORT;
			} else if (age == 1) {
				return GrowthTreeStage.MEDIUM;
			} else if (age == 2) {
				return GrowthTreeStage.TALL;
			} else if (age == 3) {
				return GrowthTreeStage.LAWN;
			}
		} else {
			return GrowthTreeStage.MEDIUM;
		}
		return null;
	}

	private GrowthStage getGrassGrowthStage() {
		if (RANDOMIZE_GRASS_GROWTH_STAGE) {
			int age = rand.nextInt(4);
			if (age == 0) {
				return GrowthStage.SHORT;
			} else if (age == 1) {
				return GrowthStage.MEDIUM;
			} else if (age == 2) {
				return GrowthStage.TALL;
			} else if (age == 3) {
				return GrowthStage.WILD;
			}

		} else {
			return GrowthStage.MEDIUM;
		}
		return null;
	}

	private FlowerType getRandomFlower() {
		int flower = rand.nextInt(1501);
		if (flower <= FLOWERS_DENSITY) {
			return FlowerType.FLOWER_1;
		} else if (flower <= 2 * FLOWERS_DENSITY) {
			return FlowerType.FLOWER_2;
		} else if (flower <= 3 * FLOWERS_DENSITY) {
			return FlowerType.FLOWER_3;
		} else if (flower <= 4 * FLOWERS_DENSITY) {
			return FlowerType.FLOWER_4;
		} else if (flower <= 5 * FLOWERS_DENSITY) {
			return FlowerType.FLOWER_5;
		} else if (flower <= 6 * FLOWERS_DENSITY) {
			return FlowerType.FLOWER_6;
		} else if (flower <= 7 * FLOWERS_DENSITY) {
			return FlowerType.FLOWER_7;
		} else if (flower <= 8 * FLOWERS_DENSITY) {
			return FlowerType.FLOWER_8;
		} else if (flower <= 9 * FLOWERS_DENSITY) {
			return FlowerType.FLOWER_9;
		} else if (flower <= 10 * FLOWERS_DENSITY) {
			return FlowerType.FLOWER_10;
		} else if (flower <= 11 * FLOWERS_DENSITY) {
			return FlowerType.FLOWER_11;
		} else if (flower <= 12 * FLOWERS_DENSITY) {
			return FlowerType.FLOWER_12;
		} else if (flower <= 13 * FLOWERS_DENSITY) {
			return FlowerType.FLOWER_13;
		} else if (flower <= 14 * FLOWERS_DENSITY) {
			return FlowerType.FLOWER_14;
		} else if (flower <= 15 * FLOWERS_DENSITY) {
			return FlowerType.FLOWER_15;
		} else {
			return FlowerType.NONE;
		}
	}

	private FoliageAge getBushAge() {
		if (RANDOMIZE_BUSH_AGE) {
			return getFoliageAge();
		} else {
			return FoliageAge.YOUNG_THREE;
		}
	}

	private FoliageAge getTreeAge() {
		if (RANDOMIZE_TREE_AGE) {
			return getFoliageAge();
		} else {
			return FoliageAge.YOUNG_THREE;
		}
	}

	private FoliageAge getFoliageAge() {
		int age = rand.nextInt(16);
		if (age == 0) {
			return FoliageAge.YOUNG_ONE;
		} else if (age == 1) {
			return FoliageAge.YOUNG_TWO;
		} else if (age == 2) {
			return FoliageAge.YOUNG_THREE;
		} else if (age == 3) {
			return FoliageAge.YOUNG_FOUR;
		} else if (age == 4) {
			return FoliageAge.MATURE_ONE;
		} else if (age == 5) {
			return FoliageAge.MATURE_TWO;
		} else if (age == 6) {
			return FoliageAge.MATURE_THREE;
		} else if (age == 7) {
			return FoliageAge.MATURE_SPROUTING;
		} else if (age == 8) {
			return FoliageAge.OLD_ONE;
		} else if (age == 9) {
			return FoliageAge.OLD_ONE_SPROUTING;
		} else if (age == 10) {
			return FoliageAge.OLD_TWO;
		} else if (age == 11) {
			return FoliageAge.OLD_TWO_SPROUTING;
		} else if (age == 12) {
			return FoliageAge.VERY_OLD;
		} else if (age == 13) {
			return FoliageAge.VERY_OLD_SPROUTING;
		} else if (age == 14) {
			return FoliageAge.OVERAGED;
		} else {
			return FoliageAge.SHRIVELLED;
		}
	}

	private static Color encodeTerrainTile(Tile tile) {
		Color color = new Color(-1);
		if (tile == Tile.TILE_SAND) {
			color = new Color(SAND_COLOR);
		} else if (tile == Tile.TILE_LAVA) {
			color = new Color(LAVA_COLOR);
		} else if (tile == Tile.TILE_CLIFF) {
			color = new Color(CLIFF_COLOR);
		} else if (tile == Tile.TILE_TUNDRA) {
			color = new Color(TUNDRA_COLOR);
		} else if (tile == Tile.TILE_STEPPE) {
			color = new Color(STEPPE_COLOR);
		} else if (tile == Tile.TILE_ROCK) {
			color = new Color(ROCK_COLOR);
		} else if (tile == Tile.TILE_CLAY) {
			color = new Color(CLAY_COLOR);
		} else if (tile == Tile.TILE_MOSS) {
			color = new Color(MOSS_COLOR);
		} else if (tile == Tile.TILE_DIRT) {
			color = new Color(DIRT_COLOR);
		} else if (tile == Tile.TILE_MYCELIUM) {
			color = new Color(MYCELIUM_COLOR);
		} else if (tile == Tile.TILE_REED) {
			color = new Color(REED_COLOR);
		} else if (tile == Tile.TILE_KELP) {
			color = new Color(KELP_COLOR);
		} else if (tile == Tile.TILE_GRASS) {
			//TODO decode flower ID
			color = new Color(GRASS_COLOR);
		} else if (tile == Tile.TILE_PEAT) {
			color = new Color(PEAT_COLOR);
		} else if (tile == Tile.TILE_MARSH) {
			color = new Color(MARSH_COLOR);
		} else if (tile == Tile.TILE_TAR) {
			color = new Color(TAR_COLOR);
		} else if (tile == Tile.TILE_SNOW) {
			color = new Color(SNOW_COLOR);
		} else if (tile == Tile.TILE_BUSH_THORN) {
			color = new Color(THORN_COLOR);
		} else if (tile == Tile.TILE_BUSH_ROSE) {
			color = new Color(ROSE_COLOR);
		} else if (tile == Tile.TILE_BUSH_OLEANDER) {
			color = new Color(OLEANDER_COLOR);
		} else if (tile == Tile.TILE_BUSH_LAVENDER) {
			color = new Color(LAVENDER_COLOR);
		} else if (tile == Tile.TILE_BUSH_GRAPE) {
			color = new Color(GRAPE_COLOR);
		} else if (tile == Tile.TILE_BUSH_CAMELLIA) {
			color = new Color(CAMELLIA_COLOR);
		} else if (tile == Tile.TILE_TREE_WILLOW) {
			color = new Color(WILLOW_COLOR);
		} else if (tile == Tile.TILE_TREE_WALNUT) {
			color = new Color(WALNUT_COLOR);
		} else if (tile == Tile.TILE_TREE_PINE) {
			color = new Color(PINE_COLOR);
		} else if (tile == Tile.TILE_TREE_OLIVE) {
			color = new Color(OLIVE_COLOR);
		} else if (tile == Tile.TILE_TREE_OAK) {
			color = new Color(OAK_COLOR);
		} else if (tile == Tile.TILE_TREE_MAPLE) {
			color = new Color(MAPLE_COLOR);
		} else if (tile == Tile.TILE_TREE_LINDEN) {
			color = new Color(LINDEN_COLOR);
		} else if (tile == Tile.TILE_TREE_LEMON) {
			color = new Color(LEMON_COLOR);
		} else if (tile == Tile.TILE_TREE_FIR) {
			color = new Color(FIR_COLOR);
		} else if (tile == Tile.TILE_TREE_CHESTNUT) {
			color = new Color(CHESTNUT_COLOR);
		} else if (tile == Tile.TILE_TREE_CHERRY) {
			color = new Color(CHERRY_COLOR);
		} else if (tile == Tile.TILE_TREE_CEDAR) {
			color = new Color(CEDAR_COLOR);
		} else if (tile == Tile.TILE_TREE_BIRCH) {
			color = new Color(BIRCH_COLOR);
		} else if (tile == Tile.TILE_TREE_APPLE) {
			color = new Color(APPLE_COLOR);
		} else {
			System.err.println("Unsupported type of terrain tile: "
					+ tile.tilename + ". Dirt will be used instead.");
			color = new Color(DIRT_COLOR);
		}
		return color;
	}

	private static Color encodeCaveTile(Tile tile) {
		Color color = new Color(-1);
		if (tile == Tile.TILE_CAVE_WALL) {
			color = new Color(CAVE_WALL_COLOR);
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
		} else if (tile == Tile.TILE_CAVE_WALL_REINFORCED) {
			color = new Color(CAVE_WALL_REINFORCED_COLOR);
		} else if (tile == Tile.TILE_CAVE_FLOOR_REINFORCED) {
			color = new Color(CAVE_FLOOR_REINFORCED_COLOR);
		} else if (tile == Tile.TILE_CAVE) {
			color = new Color(CAVE_COLOR);
		} else if (tile == Tile.TILE_CAVE_EXIT) {
			color = Tile.TILE_CAVE_EXIT.getColor();
		} else if (!tile.isCave()) {
			System.err
					.println(tile.tilename
							+ " is not a cave tile. This tile will have pink color on cave dump image.");
			color = new Color(NOT_CAVE_TILE_COLOR);
		} else {
			System.err.println("Unknown type of cave tile: " + tile.tilename
					+ ". This tile will have red color on cave dump image.");
			color = new Color(UNKNOWN_CAVE_TILE_COLOR);
		}
		return color;
	}

	private static Tile generateCaveTile(int tileNumber, short height) {
		if (height > -15) { // no veins on tiles which are too deep for surface
							// mining
			if (tileNumber % 37 == 0) {
				return Tile.TILE_CAVE_WALL_ORE_IRON;
			} else if (tileNumber % 43 == 0) {
				return Tile.TILE_CAVE_WALL_SLATE;
			} else if (tileNumber % 47 == 0) {
				return Tile.TILE_CAVE_WALL_MARBLE;
			} else if (tileNumber % 53 == 0) {
				return Tile.TILE_CAVE_WALL_ORE_COPPER;
			} else if (tileNumber % 59 == 0) {
				return Tile.TILE_CAVE_WALL_ORE_ZINC;
			} else if (tileNumber % 61 == 0) {
				return Tile.TILE_CAVE_WALL_ORE_TIN;
			} else if (tileNumber % 67 == 0) {
				return Tile.TILE_CAVE_WALL_ORE_LEAD;
			} else if (tileNumber % 71 == 0) {
				return Tile.TILE_CAVE_WALL_ORE_SILVER;
			} else if (tileNumber % 73 == 0) {
				return Tile.TILE_CAVE_WALL_ORE_GOLD;
			} else if (tileNumber % 79 == 0) {
				return Tile.TILE_CAVE_WALL_ORE_ADAMANTINE;
			} else if (tileNumber % 83 == 0) {
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
		} else if (color.getRGB() == CAVE_WALL_COLOR) {
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
		} else if (color.getRGB() == CAVE_TIN_COLOR) {
			return Tile.TILE_CAVE_WALL_ORE_TIN;
		} else if (color.getRGB() == CAVE_ZINC_COLOR) {
			return Tile.TILE_CAVE_WALL_ORE_ZINC;
		} else if (color.getRGB() == CAVE_WALL_REINFORCED_COLOR) {
			return Tile.TILE_CAVE_WALL_REINFORCED;
		} else {
			System.err.println("Unknown RGB color for cave wall: Red = "
					+ color.getRed() + " Green = " + color.getGreen()
					+ " Blue = " + color.getBlue());
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
			System.err.println("Unknown RGB color for bush " + color.getRed()
					+ ", " + color.getGreen() + ", " + color.getBlue());
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
			System.err.println("Unknown RGB color for tree " + color.getRed()
					+ ", " + color.getGreen() + ", " + color.getBlue());
			System.exit(1);
		}
		return null;
	}

	private static Tile decodeColor(Color color) {
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
		} else if (color.getRGB() == GRASS_COLOR
				|| color.getRGB() == GRASS_FLOWER1_COLOR
				|| color.getRGB() == GRASS_FLOWER2_COLOR
				|| color.getRGB() == GRASS_FLOWER3_COLOR
				|| color.getRGB() == GRASS_FLOWER4_COLOR
				|| color.getRGB() == GRASS_FLOWER5_COLOR
				|| color.getRGB() == GRASS_FLOWER6_COLOR
				|| color.getRGB() == GRASS_FLOWER7_COLOR
				|| color.getRGB() == GRASS_FLOWER8_COLOR
				|| color.getRGB() == GRASS_FLOWER9_COLOR
				|| color.getRGB() == GRASS_FLOWER10_COLOR
				|| color.getRGB() == GRASS_FLOWER11_COLOR
				|| color.getRGB() == GRASS_FLOWER12_COLOR
				|| color.getRGB() == GRASS_FLOWER13_COLOR
				|| color.getRGB() == GRASS_FLOWER14_COLOR
				|| color.getRGB() == GRASS_FLOWER15_COLOR) {
			return Tile.TILE_GRASS;
		} else {
			System.err.println("Unknown RGB color " + color.getRed() + ", "
					+ color.getGreen() + ", " + color.getBlue());
			System.exit(1);
		}
		return null;
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

		final BufferedImage bi2 = new BufferedImage(lWidth, lWidth,
				BufferedImage.TYPE_INT_RGB);
		final float[] data = new float[lWidth * lWidth * 3];

		for (int x = 0; x < lWidth; x++) {
			for (int y = lWidth - 1; y >= 0; y--) {
				final Tile tile = mapData.getCaveTile(x + xo, y + yo);

				final Color color;
				if (tile != null) {
					color = encodeCaveTile(tile);
				} else {
					System.out
							.println("Cave on tile "
									+ (x + xo)
									+ ", "
									+ (y + yo)
									+ " is not set. It will have white color on cave dump image");
					color = new Color(255, 255, 255);
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

	private static BufferedImage createRockTopographicDump(MapData mapData,
			boolean showWater, short interval) {
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

		final BufferedImage bi2 = new BufferedImage(lWidth, lWidth,
				BufferedImage.TYPE_INT_RGB);
		final float[] data = new float[lWidth * lWidth * 3];

		for (int x = 0; x < lWidth; x++) {
			for (int y = lWidth - 1; y >= 0; y--) {
				final short height = mapData.getRockHeight(x + xo, y + yo);
				final short nearHeightNX = x == 0 ? height : mapData
						.getRockHeight(x + xo - 1, y + yo);
				final short nearHeightNY = y == 0 ? height : mapData
						.getRockHeight(x + xo, y + yo - 1);
				final short nearHeightX = x == lWidth - 1 ? height : mapData
						.getRockHeight(x + xo + 1, y + yo);
				final short nearHeightY = y == lWidth - 1 ? height : mapData
						.getRockHeight(x + xo, y + yo + 1);
				boolean isControur = checkContourLine(height, nearHeightNX,
						interval)
						|| checkContourLine(height, nearHeightNY, interval)
						|| checkContourLine(height, nearHeightX, interval)
						|| checkContourLine(height, nearHeightY, interval);

				final Color color = new Color(ROCK_COLOR);
				int r = color.getRed();
				int g = color.getGreen();
				int b = color.getBlue();
				if (isControur) {
					r = 0;
					g = 0;
					b = 0;
				} else if (height < 0 && showWater) {
					r = (int) (r * 0.2f + 0.4f * 0.4f * 256f);
					g = (int) (g * 0.2f + 0.5f * 0.4f * 256f);
					b = (int) (b * 0.2f + 1.0f * 0.4f * 256f);
				}

				data[(x + y * lWidth) * 3 + 0] = r;
				data[(x + y * lWidth) * 3 + 1] = g;
				data[(x + y * lWidth) * 3 + 2] = b;
			}
		}

		bi2.getRaster().setPixels(0, 0, lWidth, lWidth, data);
		return bi2;
	}

	private static boolean checkContourLine(short h0, short h1, short interval) {
		if (h0 == h1) {
			return false;
		}
		for (int i = h0; i <= h1; i++) {
			if (i % interval == 0) {
				return true;
			}
		}
		return false;
	}

	public static BufferedImage exportRockMap(MapData mapData) {

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

		final short[] pixels = new short[lWidth * lWidth];

		for (int x = 0; x < lWidth; x++) {
			for (int y = lWidth - 1; y >= 0; y--) {
				short rockHeight = mapData.getRockHeight(x + xo, y + yo);
				pixels[x + y * lWidth] = (short) (rockHeight - Short.MAX_VALUE);
			}
		}

		ColorModel colorModel = new ComponentColorModel(
				ColorSpace.getInstance(ColorSpace.CS_GRAY), new int[] { 16 },
				false, false, Transparency.OPAQUE, DataBuffer.TYPE_USHORT);
		DataBufferUShort db = new DataBufferUShort(pixels, pixels.length);
		WritableRaster raster = Raster.createInterleavedRaster(db,
				mapData.getWidth(), mapData.getHeight(), mapData.getWidth(), 1,
				new int[1], null);
		return new BufferedImage(colorModel, raster, false, null);
	}

	public static BufferedImage exportHeightMap(MapData mapData) {

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

		final short[] pixels = new short[lWidth * lWidth];

		for (int x = 0; x < lWidth; x++) {
			for (int y = lWidth - 1; y >= 0; y--) {
				short height = mapData.getSurfaceHeight(x + xo, y + yo);
				pixels[x + y * lWidth] = (short) (height - Short.MAX_VALUE);
			}
		}

		ColorModel colorModel = new ComponentColorModel(
				ColorSpace.getInstance(ColorSpace.CS_GRAY), new int[] { 16 },
				false, false, Transparency.OPAQUE, DataBuffer.TYPE_USHORT);
		DataBufferUShort db = new DataBufferUShort(pixels, pixels.length);
		WritableRaster raster = Raster.createInterleavedRaster(db,
				mapData.getWidth(), mapData.getHeight(), mapData.getWidth(), 1,
				new int[1], null);
		return new BufferedImage(colorModel, raster, false, null);
	}

	private static BufferedImage exportTerrainMap(MapData mapData) {
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

		final BufferedImage bi2 = new BufferedImage(lWidth, lWidth,
				BufferedImage.TYPE_INT_RGB);
		final float[] data = new float[lWidth * lWidth * 3];

		for (int x = 0; x < lWidth; x++) {
			for (int y = lWidth - 1; y >= 0; y--) {
				final Tile tile = mapData.getSurfaceTile(x + xo, y + yo);

				final Color color;
				if (tile != null) {
					if (tile == Tile.TILE_GRASS) {
						color = encodeTerrainTile(tile);
						// TODO: show different types of flowers in different colors
					} else {
						color = encodeTerrainTile(tile);
					}
				} else {
					System.out
							.println("Terrain on tile "
									+ (x + xo)
									+ ", "
									+ (y + yo)
									+ " is not set. Dirt will be used on the terrain map.");
					color = new Color(DIRT_COLOR);
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

	private static BufferedImage exportCaveMap(MapData mapData) {
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

		final BufferedImage bi2 = new BufferedImage(lWidth, lWidth,
				BufferedImage.TYPE_INT_RGB);
		final float[] data = new float[lWidth * lWidth * 3];

		for (int x = 0; x < lWidth; x++) {
			for (int y = lWidth - 1; y >= 0; y--) {
				final Tile tile = mapData.getCaveTile(x + xo, y + yo);

				final Color color;
				if (tile != null) {
					color = encodeCaveTile(tile);
				} else {
					System.out
							.println("Cave on tile "
									+ (x + xo)
									+ ", "
									+ (y + yo)
									+ " is not set. The tile will have white color on cave dump image");
					color = new Color(255, 255, 255);
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

}
