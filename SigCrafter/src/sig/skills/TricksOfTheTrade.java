package sig.skills;

import sig.Craft;
import sig.Skill;
import sig.Status;

public class TricksOfTheTrade extends Skill {

	public TricksOfTheTrade(String name, int CPCost, boolean guaranteed, int lvReq) {
		super(name, CPCost, guaranteed, lvReq);
	}

	@Override
	public boolean canBeUsed(Craft c) {
		return super.canBeUsed(c)&&(c.craft_status==Status.GOOD||c.craft_status==Status.EXCELLENT);
	}

	@Override
	public void useSkill(Craft c) {
		super.useSkill(c);
		c.craft_cp = Math.min(c.cp,c.craft_cp+20);
	}

}
