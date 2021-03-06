package me.daddychurchill.CityWorld.Support;

import org.bukkit.util.noise.NoiseGenerator;

import me.daddychurchill.CityWorld.CityWorldGenerator;

public abstract class AbstractCachedYs {

	// extremes
	protected int minHeight = Integer.MAX_VALUE;
	protected int minHeightX = 0;
	protected int minHeightZ = 0;
	protected int maxHeight = Integer.MIN_VALUE;
	protected int maxHeightX = 0;
	protected int maxHeightZ = 0;
	protected int averageHeight;
	protected int segmentWidth;

	protected final static int width = AbstractBlocks.sectionBlockWidth;
	protected double[][] blockYs = new double[width][width];

	public AbstractCachedYs(CityWorldGenerator generator, int chunkX, int chunkZ) {

		// total height
		int sumHeight = 0;

		// compute offset to start of chunk
		int originX = chunkX * width;
		int originZ = chunkZ * width;

		// calculate the Ys for this chunk
		for (int x = 0; x < width; x++) {
			for (int z = 0; z < width; z++) {

				// how high are we?
				blockYs[x][z] = generator.shapeProvider.findPerciseY(generator, originX + x, originZ + z);

				// keep the tally going
				sumHeight += blockYs[x][z];
				calcTally(blockYs[x][z], x, z);
			}
		}

		// what was the average height
		averageHeight = sumHeight / (width * width);
	}

	private void calcTally(double realY, int x, int z) {
		int y = NoiseGenerator.floor(realY);
		if (y < minHeight) {
			minHeight = y;
			minHeightX = x;
			minHeightZ = z;
		}
		if (y > maxHeight) {
			maxHeight = y;
			maxHeightX = x;
			maxHeightZ = z;
		}
	}
	
	public int getMaxYWithin(int x1, int x2, int z1, int z2) {
		assert(x1 >= 0 && x2 <= 15 && z1 >= 0 && z2 <= 15);
		int maxY = Integer.MIN_VALUE;
		for (int x = x1; x < x2; x++)
			for (int z = z1; z < z2; z++) {
				int y = getBlockY(x, z);
				if (y > maxY)
					maxY = y;
			}
		return maxY;
	}

	public int getBlockY(int x, int z) {
		return NoiseGenerator.floor(blockYs[x][z]);
	}

	public double getPerciseY(int x, int z) {
		return blockYs[x][z];
	}

	public Point getHighPoint() {
		return new Point(maxHeightX, maxHeight, maxHeightZ);
	}

	public Point getLowPoint() {
		return new Point(minHeightX, minHeight, minHeightZ);
	}

	public int getSegment(int x, int z) {
		return 0;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public int getAverageHeight() {
		return averageHeight;
	}

	public int getSegmentWidth() {
		return segmentWidth;
	}

//	public void lift(int h) {
//		// total height
//		int sumHeight = 0;
//		
//		// change the height
//		for (int x = 0; x < width; x++) {
//			for (int z = 0; z < width; z++) {
//				blockYs[x][z] = blockYs[x][z] + h;
//				
//				// keep the tally going
//				sumHeight += blockYs[x][z];
//				calcTally(blockYs[x][z], x, z);
//			}
//		}
//		
//		// what was the average height
//		averageHeight = sumHeight / (width * width);
//	}

//	public void draw(AbstractBlocks chunk) {
//		for (int x = 0; x < width; x++) {
//			for (int z = 0; z < width; z++) {
//				chunk.setBlock(x, getBlockY(x, z), z, Material.GOLD_BLOCK);
//			}
//		}
//		chunk.setBlock(minHeightX, minHeight, minHeightZ, Material.DIAMOND_BLOCK);
//		chunk.setBlock(maxHeightX, maxHeight, maxHeightZ, Material.LAPIS_BLOCK);
//		chunk.setBlock(0, averageHeight - 1, 0, Material.COAL_BLOCK);
//		chunk.setBlock(0, averageHeight + 1, 0, Material.COAL_BLOCK);
//	}
//	
//	public void report(CityWorldGenerator generator, String prefix) {
//		generator.reportFormatted("%s minHeight = %s maxHeight = %s averageHeight = %s streetLevel = %s", 
//				prefix, minHeight, maxHeight, averageHeight, generator.streetLevel);
//	}
//
//	public void reportMatrix(CityWorldGenerator generator, String prefix) {
//		for (int z = 0; z < width; z++) {
//			StringBuilder line = new StringBuilder(String.format("%s [%2s] =", prefix, z));
//			for (int x = 0; x < width; x++) {
//				line.append(String.format(" %4s", getBlockY(x, z)));
//			}
//			generator.reportMessage(line.toString());
//		}
//	}
}
