package sig.skills;

import sig.Buff;
import sig.Craft;
import sig.Skill;

public class WasteNot extends Skill {

	public WasteNot(String name, int CPCost, boolean guaranteed, int lvReq) {
		super(name, CPCost, guaranteed, lvReq);
	}

	@Override
	public void useSkill(Craft c) {
		super.useSkill(c);
		c.BuffList.put("Waste Not",new Buff("Waste Not",4));
	}

}
