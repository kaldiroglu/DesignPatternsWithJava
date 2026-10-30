
package dev.kaldiroglu.dp.behavioral.iterator.fileSystem;

public class Main {

	public static void main(String[] args) {
		Directory userDir = new Directory("/Users/akin");
		userDir.list();
		
		Directory devDir = new Directory("Dev", userDir); // /Users/akin/Dev directory
		File file1 = new File("Readme.txt", devDir);	// /Users/akin/Dev/Readme.txt file
		File file2 = new File("Report.docs", devDir);	// /Users/akin/Dev/Report.docs file
		File file3 = new File("Selam.java", devDir);	// /Users/akin/Dev/Selam.java file
		
		Directory reportDir = new Directory("Reports", devDir); // /Users/akin/Dev/Reports directory
		File file4 = new File("ImportantReport.docs", reportDir);	// /Users/akin/Dev/Reports/ImportantReport.docs file
		File file5 = new File("SelamTest.java", reportDir);	// /Users/akin/Dev/Reports/SelamTest.java file

		System.out.println("\nIterating");
		DirectoryIterator iterator = devDir.iterator();
		while(iterator.hasNext())
			System.out.println(iterator.next());
		
		System.out.println("\nIterating");
		iterator = reportDir.iterator();
		while(iterator.hasNext())
			System.out.println(iterator.next());
	}
}
