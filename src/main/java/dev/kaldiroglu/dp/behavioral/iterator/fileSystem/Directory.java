
package dev.kaldiroglu.dp.behavioral.iterator.fileSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Directory extends StorageElement {
	List<Storage> elements = new ArrayList<>();

	public Directory(String name) {
		super(name, null);
		directory = true;
	}

	public Directory(String name, Directory parent) {
		super(name, parent);
		directory = true;
	}

	/** A copy with its own copies of the elements, in no directory until it is added to one. */
	@Override
	public Storage copy() {
		Directory copiedDirectory = (Directory) super.copy();
		copiedDirectory.elements = new ArrayList<>();
		for (Storage element : elements) {
			StorageElement copiedElement = (StorageElement) element.copy();
			copiedElement.parent = copiedDirectory;
			copiedDirectory.add(copiedElement);
		}
		return copiedDirectory;
	}

	public void add(Storage element) {
		elements.add(element);
	}

	public void delete(Storage element) {
		elements.remove(element);
	}

	public void list() {
		System.out.println("\nList of the directory: " + name);
		for (Storage element : elements) {
			StorageElement storageElement = (StorageElement) element;
			if (storageElement.isDirectory())
				System.out.format("%-20s %10s %s", storageElement, "dir", '\n');
			else
				System.out.format("%-20s %s", storageElement, '\n');
		}
	}

	List<Storage> elements() {
		return Collections.unmodifiableList(elements);
	}

	public DirectoryIterator<Storage> iterator() {
		DirectoryIterator<Storage> iterator = new DirectoryIterator<>(this);
		return iterator;
	}
}
