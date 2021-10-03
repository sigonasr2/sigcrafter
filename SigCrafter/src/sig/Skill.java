package sig;

public class Skill implements SkillInterface{
	public String name;
	public int CPCost;
	public boolean guaranteed;
	public int lvReq;
	public int modifier;
	public int key;
	public Skill(String name, int CPCost, boolean guaranteed, int lvReq, int modifier, int key) {
		this.name = name;
		this.CPCost = CPCost;
		this.guaranteed = guaranteed;
		this.lvReq = lvReq;
		this.modifier=modifier;
		this.key=key;
	}
	public Skill(String name, int CPCost, boolean guaranteed, int lvReq, int key) {
		this(name,CPCost,guaranteed,lvReq,-1,key);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCPCost() {
		return CPCost;
	}
	public void setCPCost(int cPCost) {
		CPCost = cPCost;
	}
	public boolean isGuaranteed() {
		return guaranteed;
	}
	public void setGuaranteed(boolean guaranteed) {
		this.guaranteed = guaranteed;
	}
	public int getLvReq() {
		return lvReq;
	}
	public void setLvReq(int lvReq) {
		this.lvReq = lvReq;
	}
	@Override
	public void useSkill(Craft c) {
		c.craft_cp -= CPCost;
		c.progress_mult=1;
		c.quality_mult=1;
		c.durability_mult=1;
		c.control = c.base_control;
		for (String key : c.BuffList.keySet()) {
			if (c.BuffList.get(key).stackCount>0 && !key.equalsIgnoreCase("Inner Quiet") && !key.equalsIgnoreCase("Name of the Elements Has Been Used")) {
				c.BuffList.get(key).stackCount-=1;
			}
		}
		c.control += c.base_control * 0.2 * c.BuffList.get("Inner Quiet").stackCount;
		c.quality_mult += c.craft_status==Status.GOOD?0.5:c.craft_status==Status.EXCELLENT?3.0:0;
		c.progress_mult += c.BuffList.get("Veneration").stackCount>0?0.5:0;
		c.quality_mult += c.BuffList.get("Great Strides").stackCount>0?1:0;
		c.quality_mult += c.BuffList.get("Innovation").stackCount>0?0.5:0;
		c.durability_mult = c.BuffList.get("Waste Not").stackCount>0?0.5:1;
		c.SkillList.add(this);
	}
	@Override
	public boolean canBeUsed(Craft c) {
		return c.craft_cp>=CPCost;
	}	
}
