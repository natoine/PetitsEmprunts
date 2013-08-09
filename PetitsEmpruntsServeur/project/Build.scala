import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "PetitsEmpruntsServeur"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
     	// Add your project dependencies here,
     	javaCore,
     	javaJdbc,
    	javaEbean,
     	"com.typesafe" %% "play-plugins-mailer" % "2.1.0",
     	"be.objectify" %% "deadbolt-java" % "2.1-RC2"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      	// Add your own project settings here   
		resolvers += "Maven repository" at "http://morphia.googlecode.com/svn/mavenrepo/",
     	resolvers += "MongoDb Java Driver Repository" at "http://repo1.maven.org/maven2/org/mongodb/mongo-java-driver/",
     	resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
     	resolvers += Resolver.url("Objectify Play Repository", url("http://schaloner.github.com/releases/"))(Resolver.ivyStylePatterns),
  		resolvers += Resolver.url("Objectify Play Snapshot Repository", url("http://schaloner.github.com/snapshots/"))(Resolver.ivyStylePatterns)
    )

}
