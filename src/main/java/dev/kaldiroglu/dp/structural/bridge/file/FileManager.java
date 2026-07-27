package dev.kaldiroglu.dp.structural.bridge.file;

public interface FileManager {
	
	public void setFileAdapter(FileProviderAdaptor adaptor);
	
	public void readFile();
	
	public void writeFile();
	
	public void updateFile();

}
