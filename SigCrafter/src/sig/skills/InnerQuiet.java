package sig.skills;

import sig.Buff;
import sig.Craft;
import sig.Skill;

public class InnerQuiet extends Skill {
	public InnerQuiet(String name, int CPCost, boolean guaranteed, int lvReq) {
		super(name, CPCost, guaranteed, lvReq);
	}

	@Override
	public boolean canBeUsed(Craft c) {
		return super.canBeUsed(c)&&c.BuffList.get("Inner Quiet").stackCount==0;
	}

	@Override
	public void useSkill(Craft c) {
		super.useSkill(c);
		c.BuffList.put("Inner Quiet",new Buff("Inner Quiet",1));
	}
}
