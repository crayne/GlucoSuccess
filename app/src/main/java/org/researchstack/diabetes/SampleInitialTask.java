package org.researchstack.diabetes;
import android.content.Context;

import org.researchstack.backbone.answerformat.AnswerFormat;
import org.researchstack.backbone.answerformat.BooleanAnswerFormat;
import org.researchstack.backbone.answerformat.ChoiceAnswerFormat;
import org.researchstack.backbone.model.Choice;
import org.researchstack.backbone.step.FormStep;
import org.researchstack.backbone.step.InstructionStep;
import org.researchstack.backbone.step.QuestionStep;
import org.researchstack.backbone.step.Step;
import org.researchstack.backbone.task.OrderedTask;
import org.researchstack.skin.R;

public class SampleInitialTask extends OrderedTask
{

    public SampleInitialTask(String identifier)
    {
        super(identifier);

        //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
        // Intro step
        //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

        InstructionStep step = new InstructionStep("intro",
                "What to Expect",
                "Choose which general health information the app may read from and write to HealthKit");

        // Add to Task
        //addStep(step);

        //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
        // Basic Info Form step
        //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

        FormStep basicInfoForm = new FormStep("basicInfo", "GlucoSuccess Permissions", "");

        // Allow writing data
        AnswerFormat allowWriteFormat = new ChoiceAnswerFormat(AnswerFormat.ChoiceAnswerStyle.MultipleChoice,
                new Choice<>("Don't allow writing data", 0),
                new Choice<>("Height", 1),
                new Choice<>("Weight", 2));
        QuestionStep allowWriteStep = new QuestionStep("allowWrite", "Allow GlucoSuccess to Write Data", allowWriteFormat);

        // Allow reading data
        AnswerFormat allowReadFormat = new ChoiceAnswerFormat(AnswerFormat.ChoiceAnswerStyle.MultipleChoice,
                new Choice<>("Don't allow reading data", 0),
                new Choice<>("BloodGlucose", 1),
                new Choice<>("Carbohydrates", 2),
                new Choice<>("Date of Birth", 3),
                new Choice<>("Dietary Calories", 4),
                new Choice<>("Dietary Sugar", 5),
                new Choice<>("Height", 6),
                new Choice<>("Sex", 7),
                new Choice<>("Sleep Analysis", 8),
                new Choice<>("Steps", 9),
                new Choice<>("Weight", 10),
                new Choice<>("Workouts", 11));


                QuestionStep allowReadStep = new QuestionStep("allowRead", "Allow GlucoSuccess to Read Data", allowReadFormat);


        // Set items on FormStep
        basicInfoForm.setOptional(true);
        basicInfoForm.setFormSteps(allowWriteStep, allowReadStep);

        // Add to Task
        //addStep(basicInfoForm);

        //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
        // Eye and Hair color form step
        //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

        FormStep hairEyesForm = new FormStep("hairEyesInfo", "Natural Hair and Eye Color", "");

        // Hair Color
        AnswerFormat hairColorFormat = new ChoiceAnswerFormat(AnswerFormat.ChoiceAnswerStyle.SingleChoice,
                new Choice<>("Red Hair", "redHair"),
                new Choice<>("Blond Hair", "blondeHair"),
                new Choice<>("Brown Hair", "brownHair"),
                new Choice<>("Black Hair", "blackHair"));
        QuestionStep hairColorStep = new QuestionStep("hairColor", "Hair Color", hairColorFormat);

        AnswerFormat eyeColorFormat = new ChoiceAnswerFormat(AnswerFormat.ChoiceAnswerStyle.SingleChoice,
                new Choice<>("Blue Eyes", "blueEyes"),
                new Choice<>("Green Eyes", "greenEyes"),
                new Choice<>("Brown Eyes", "brownEyes"));
        QuestionStep eyeColorStep = new QuestionStep("eyeColor", "Eye Color", eyeColorFormat);

        // Set items on FormStep
        hairEyesForm.setOptional(true);
        hairEyesForm.setFormSteps(hairColorStep, eyeColorStep);

        // Add to Task
        //addStep(hairEyesForm);

        //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
        // Profession Step
        //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

        // iOS defines this as a single choice, should be MultiChoice
        AnswerFormat professionFormat = new ChoiceAnswerFormat(AnswerFormat.ChoiceAnswerStyle.SingleChoice,
                new Choice<>("Chocolate", "chocolate"),
                new Choice<>("Vanilla", "vanilla"),
                new Choice<>("Strawberry", "strawberry"),
                new Choice<>("Cookies & Cream", "cookies_cream"),
                new Choice<>("I am Robot, what is ice cream?", "robot"));
        QuestionStep professionStep = new QuestionStep("profession",
                "What is your favorite flavor of ice cream?",
                professionFormat);
        professionStep.setOptional(true);

        // Add to Task
        //addStep(professionStep);

        //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
        // Medical Info Form Step
        //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

        FormStep medicalInfoForm = new FormStep("medicalInfo", "Medical Information", "");

        BooleanAnswerFormat booleanAnswerFormat = new BooleanAnswerFormat();

        QuestionStep robotStep = new QuestionStep("confirmRobot",
                "Are you a robot?",
                booleanAnswerFormat);
        QuestionStep autoImmuneStep = new QuestionStep("feelings",
                "Does your robot body feel?",
                booleanAnswerFormat);
        QuestionStep immunocompromisedStep = new QuestionStep("arnold",
                "Are you stronger than a T-1000?",
                booleanAnswerFormat);

        // Set items on FormStep
        medicalInfoForm.setOptional(true);
        medicalInfoForm.setFormSteps(robotStep, autoImmuneStep, immunocompromisedStep);

        // Add to Task
        //addStep(medicalInfoForm);

        //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
        // Thank You Step
        //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

        InstructionStep thankYouStep = new InstructionStep("thankYou",
                "Thank You!",
                "Your participation in this study is helping us to better understand risks of becoming a robot\n\nYour task now is to take robot surveys each month. You don't have to get them all, but the more the better!\n\nHappy robot-ing!");
        // Add to Task
        //addStep(thankYouStep);

    }

    @Override
    public String getTitleForStep(Context context, Step step)
    {
        int currentIndex = getSteps().indexOf(step) + 1;
        return context.getString(R.string.rss_format_step_title, currentIndex, getSteps().size());
    }

}
