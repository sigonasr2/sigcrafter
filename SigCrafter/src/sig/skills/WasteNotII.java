package sig.skills;

import sig.Buff;
import sig.Craft;
import sig.Skill;

public class WasteNotII extends Skill {
	public WasteNotII(String name, int CPCost, boolean guaranteed, int lvReq, int modifier, int key) {
		super(name, CPCost, guaranteed, lvReq, modifier, key);
	}

	public WasteNotII(String name, int CPCost, boolean guaranteed, int lvReq, int key) {
		super(name, CPCost, guaranteed, lvReq, key);
	}

	@Override
	public void useSkill(Craft c) {
		super.useSkill(c);
		c.BuffList.put("Waste Not",new Buff("Waste Not",8));
		super.updateBuffs(c);
	}
}
