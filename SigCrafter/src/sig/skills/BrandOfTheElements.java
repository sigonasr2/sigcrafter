package sig.skills;

import sig.Craft;
import sig.Skill;

public class BrandOfTheElements extends Skill {

	public BrandOfTheElements(String name, int CPCost, boolean guaranteed, int lvReq, int modifier, int key) {
		super(name, CPCost, guaranteed, lvReq, modifier, key);
	}

	public BrandOfTheElements(String name, int CPCost, boolean guaranteed, int lvReq, int key) {
		super(name, CPCost, guaranteed, lvReq, key);
	}

	@Override
	public void useSkill(Craft c) {
		c.craft_progress += c.base_progress * c.progress_mult * c.BuffList.get("Name of the Elements").getStackCount()>0?((1-(c.craft_progress/c.progress_goal))*2):1;
		c.craft_durability -= 10 * c.durability_mult;
		super.useSkill(c);
		super.updateBuffs(c);
	}

}
