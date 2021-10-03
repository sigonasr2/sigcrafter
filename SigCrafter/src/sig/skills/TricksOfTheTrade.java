package sig.skills;

import sig.Craft;
import sig.Skill;
import sig.Status;

public class TricksOfTheTrade extends Skill {

	public TricksOfTheTrade(String name, int CPCost, boolean guaranteed, int lvReq, int modifier, int key) {
		super(name, CPCost, guaranteed, lvReq, modifier, key);
	}

	public TricksOfTheTrade(String name, int CPCost, boolean guaranteed, int lvReq, int key) {
		super(name, CPCost, guaranteed, lvReq, key);
	}

	@Override
	public boolean canBeUsed(Craft c) {
		return super.canBeUsed(c)&&(c.craft_status==Status.GOOD||c.craft_status==Status.EXCELLENT);
	}

	@Override
	public void useSkill(Craft c) {
		c.craft_cp = Math.min(c.cp,c.craft_cp+20);
		super.useSkill(c);
		super.updateBuffs(c);
	}

}
