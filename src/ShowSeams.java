/*************************************************************************
 *  Compilation:  javac ShowSeams.java
 *  Execution:    java ShowSeams input.png
 *  Dependencies: SeamCarver.java SCUtility.java Picture.java StdDraw.java
 *                
 *
 *  Read image from file specified as command line argument. Show 3 images 
 *  original image as well as horizontal and vertical seams of that image.
 *  Each image hides the previous one - drag them to see all three.
 *
 *************************************************************************/

public class ShowSeams {

	private static void showHoriontalSeam(SeamCarver sc)
	{
		Picture ep = SCUtility.toEnergyPicture(sc);
		int[] horizontalSeam = sc.findHorizontalSeam();
		Picture epOverlay = SCUtility.seamOverlay(ep, true, horizontalSeam);
		epOverlay.show();
	}


	private static void showVerticalSeam(SeamCarver sc)
	{
		Picture ep = SCUtility.toEnergyPicture(sc);
		int[] verticalSeam = sc.findVerticalSeam();
		Picture epOverlay = SCUtility.seamOverlay(ep, false, verticalSeam);
		epOverlay.show();
	}

	public static void main(String[] args)
	{
		Picture inputImg = new Picture(args[0]);
		System.out.printf("image is %d columns by %d rows\n", inputImg.width(), inputImg.height());
		inputImg.show();		
		SeamCarver sc = new SeamCarver(inputImg);
		
		System.out.printf("Displaying horizontal seam calculated.\n");
		showHoriontalSeam(sc);

		System.out.printf("Displaying vertical seam calculated.\n");
		showVerticalSeam(sc);

	}

}