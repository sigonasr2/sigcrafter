package sig.skills;

import sig.Buff;
import sig.Craft;
import sig.Skill;

public class GreatStrides extends Skill {
	public GreatStrides(String name, int CPCost, boolean guaranteed, int lvReq) {
		super(name, CPCost, guaranteed, lvReq);
	}

	@Override
	public void useSkill(Craft c) {
		super.useSkill(c);
		c.BuffList.put("Great Strides",new Buff("Great Strides",3));
	}
}
