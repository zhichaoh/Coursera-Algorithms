/*************************************************************************
 *  Compilation:  javac ShowEnergy.java
 *  Execution:    java ShowEnergy input.png
 *  Dependencies: SeamCarver.java SCUtility.java Picture.java StdDraw.java
 *                
 *
 *  Read image from file specified as command line argument. Show original
 *  image (only useful if image is large enough).
 *
 *************************************************************************/

public class ShowEnergy {

	public static void main(String[] args)
	{
		Picture inputImg = new Picture(args[0]);
		System.out.printf("image is %d columns by %d rows\n", inputImg.width(), inputImg.height());
		inputImg.show();		
		SeamCarver sc = new SeamCarver(inputImg);
		
		System.out.printf("Displaying energy calculated for each pixel.\n");
		SCUtility.showEnergy(sc);

	}

}