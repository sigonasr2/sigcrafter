package sig.skills;

import sig.Craft;
import sig.Skill;

public class BasicSynthesis extends Skill {

	public BasicSynthesis(String name, int CPCost, boolean guaranteed, int lvReq, int modifier, int key) {
		super(name, CPCost, guaranteed, lvReq, modifier, key);
	}

	public BasicSynthesis(String name, int CPCost, boolean guaranteed, int lvReq, int key) {
		super(name, CPCost, guaranteed, lvReq, key);
	}

	@Override
	public void useSkill(Craft c) {
		c.craft_progress += c.base_progress * c.progress_mult * 1.2;
		c.craft_durability -= 10 * c.durability_mult;
		super.useSkill(c);
		super.updateBuffs(c);
	}

}
