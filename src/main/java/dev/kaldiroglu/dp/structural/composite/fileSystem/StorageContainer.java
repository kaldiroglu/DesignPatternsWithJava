
package dev.kaldiroglu.dp.structural.composite.fileSystem;

import dev.kaldiroglu.dp.structural.composite.fileSystem.iterator.DirectoryIterator;

import java.util.List;

public interface StorageContainer {
	
	public void add(Storage element);
	
	public void delete(Storage element);
	
	public void list();
	
	public List<Storage> elements();
	
	public DirectoryIterator iterator();

}
