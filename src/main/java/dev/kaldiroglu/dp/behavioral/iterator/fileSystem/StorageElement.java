
package dev.kaldiroglu.dp.behavioral.iterator.fileSystem;

public abstract class StorageElement implements Storage, Cloneable {
	protected String name;
	protected Directory parent;
	protected boolean directory;

	public StorageElement(String name, Directory parent) {
		this.name = name;
		this.parent = parent;
		if(parent != null)
			parent.add(this);
	}

	@Override
	public void rename(String newName) {
		setName(newName);
	}

	@Override
	public void save() {
		System.out.println("Saving the element.");
	}

	@Override
	public void delete() {
		if (parent != null)
			parent.delete(this);
		parent = null;
	}

	@Override
	public Storage copy() {
		try {
			StorageElement copiedElement = (StorageElement) clone();
			copiedElement.parent = null;
			return copiedElement;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);	// StorageElement is Cloneable
		}
	}

	@Override
	public void move(Directory target) {
		delete();
		parent = target;
		target.add(this);
	}
	
	public boolean isDirectory(){
		return directory;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Directory getParent() {
		return parent;
	}

	public void setParent(Directory parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return name;
	}
}
