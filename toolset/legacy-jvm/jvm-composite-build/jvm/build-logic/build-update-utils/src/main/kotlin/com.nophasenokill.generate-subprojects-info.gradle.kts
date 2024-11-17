import com.nophasenokill.buildutils.tasks.GenerateSubprojectsInfo
import com.nophasenokill.buildutils.tasks.CheckSubprojectsInfo

tasks.register<GenerateSubprojectsInfo>(GenerateSubprojectsInfo.TASK_NAME)
tasks.register<CheckSubprojectsInfo>("checkSubprojectsInfo")
