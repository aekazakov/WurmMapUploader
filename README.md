# WurmMapUploader
very basic uploader of heightmap, terrain and cave data to Wurm Unlimited map directory

Requires WurmModServerApi (https://github.com/codeclubab/WurmModServerApi)

Heightmap must be in 16 bit greyscale PNG format.
Terrain and cave maps must be in 24 or 32 bit color PNG format.

Note that some colors (especially plants and cave walls) differ from those defined for tiles in common.jar library of WurmModServerApi

Colors for terrain map:
Terrain type	Name	Red	Green	Blue
TILE_BUSH_CAMELLIA	Camellia bush	41	58	12
TILE_BUSH_GRAPE	Grape bush	41	58	22
TILE_BUSH_LAVENDER	Lavender bush	41	58	32
TILE_BUSH_OLEANDER	Oleander bush	41	58	42
TILE_BUSH_ROSE	Rose bush	41	58	52
TILE_BUSH_THORN	Thorn bush	41	58	62
TILE_CLAY	Clay	113	124	118
TILE_CLIFF	Cliff	155	151	148
TILE_DIRT	Dirt	75	63	47
TILE_GRASS	Grass	54	101	3
TILE_KELP	Kelp	54	101	23
TILE_LAVA	Lava	215	51	30
TILE_MARSH	Marsh	43	101	72
TILE_MOSS	Moss	106	142	56
TILE_MYCELIUM	Mycelium	71	2	51
TILE_PEAT	Peat	54	39	32
TILE_REED	Reed	54	101	63
TILE_ROCK	Rock	114	110	107
TILE_SAND	Sand	160	147	109
TILE_SNOW	Snow	255	255	255
TILE_STEPPE	Steppe	114	117	67
TILE_TAR	Tar	18	21	40
TILE_TREE_APPLE	Apple tree	41	58	92
TILE_TREE_BIRCH	Birch tree	41	58	102
TILE_TREE_CEDAR	Cedar tree	41	58	112
TILE_TREE_CHERRY	Cherry tree	41	58	122
TILE_TREE_CHESTNUT	Chestnut tree	41	58	132
TILE_TREE_FIR	Fir tree	41	58	142
TILE_TREE_LEMON	Lemon tree	41	58	152
TILE_TREE_LINDEN	Linden tree	41	58	162
TILE_TREE_MAPLE	Maple tree	41	58	172
TILE_TREE_OAK	Oak tree	41	58	182
TILE_TREE_OLIVE	Olive tree	41	58	192
TILE_TREE_PINE	Pine tree	41	58	202
TILE_TREE_WALNUT	Walnut tree	41	58	212
TILE_TREE_WILLOW	Willow tree	41	58	222
TILE_TUNDRA	Tundra	118	135	109

Colors for cave map:
Terrain type	Name	Red	Green	Blue
TILE_CAVE_WALL	Cave wall	127	127	127
TILE_CAVE_WALL_LAVA	Lava wall	215	51	30
TILE_CAVE_WALL_MARBLE	Marble wall	238	238	238
TILE_CAVE_WALL_ORE_ADAMANTINE	Adamantine vein	41	58	122
TILE_CAVE_WALL_ORE_COPPER	Copper vein	62	116	107
TILE_CAVE_WALL_ORE_GLIMMERSTEEL	Glimmersteel vein	200	200	200
TILE_CAVE_WALL_ORE_GOLD	Gold vein	255	216	0
TILE_CAVE_WALL_ORE_IRON	Iron vein	46	32	21
TILE_CAVE_WALL_ORE_LEAD	Lead vein	85	84	78
TILE_CAVE_WALL_ORE_SILVER	Silver vein	165	165	165
TILE_CAVE_WALL_ORE_TIN	Tin vein	41	58	12
TILE_CAVE_WALL_ORE_ZINC	Zinc vein	104	125	134
TILE_CAVE_WALL_SLATE	Slate wall	30	30	30

Uploader generates dump images after data upload.