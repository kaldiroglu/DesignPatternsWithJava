package dev.kaldiroglu.dp.behavioral.visitor.file.pattern1;

/** A second operation on the same files: anyone may read a text file, only an admin an XML file. */
public class AccessVisitor implements Visitor {
	private final boolean admin;

	public AccessVisitor(boolean admin) {
		this.admin = admin;
	}

	@Override
	public boolean visit(TextFile file) {
		System.out.println("Anyone may read the file: " + file.getName());
		return true;
	}

	@Override
	public boolean visit(XMLFile file) {
		System.out.println((admin ? "An admin may read the file: " : "Only an admin may read the file: ") + file.getName());
		return admin;
	}
}
