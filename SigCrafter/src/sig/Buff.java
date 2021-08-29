package sig;

public class Buff {
	public String name="";
	public int stackCount=0;
	public Buff(String name, int stackCount) {
		this.name = name;
		this.stackCount = stackCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStackCount() {
		return stackCount;
	}
	public void setStackCount(int stackCount) {
		this.stackCount = stackCount;
	}
	@Override
	public String toString() {
		return "Buff [" + name + " (" + stackCount + ")]";
	}
}
