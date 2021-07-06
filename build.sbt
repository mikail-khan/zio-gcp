import BuildHelper._
import sbt.Keys.semanticdbEnabled

inThisBuild(
  List(
    organization := "dev.zio",
    homepage := Some(url("https://zio.github.io/zio-gcp/")),
    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer(
        "mikail-khan",
        "Mika'il Khan",
        "mikail.dev.io@gmail.com",
        url("https://github.com/mikail-khan")
      )
    ),
    pgpPassphrase := sys.env.get("PGP_PASSWORD").map(_.toArray),
    pgpPublicRing := file("/tmp/public.asc"),
    pgpSecretRing := file("/tmp/secret.asc"),
    scmInfo := Some(
      ScmInfo(url("https://github.com/zio/zio-gcp/"), "scm:git:git@github.com:zio/zio-gcp.git")
    ),
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    scalacOptions += "-Ywarn-unused-import"
  )
)

addCompilerPlugin(scalafixSemanticdb)

addCommandAlias("fmt", "all scalafmtSbt scalafmtAll")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheckAll")
addCommandAlias("fix", "all compile:scalafix test:scalafix")
addCommandAlias(
  "fixCheck",
  "; compile:scalafix --check ; test:scalafix --check"
)

lazy val root = project
  .in(file("."))
  .settings(skip in publish := true)
  .aggregate(
    core,
    firestore,
    pubsub,
    storage,
    redis
  )

lazy val core = project
  .in(file("modules/core"))
  .settings(stdSettings("zio-gcp-core"))
  .settings(buildInfoSettings("zio.gcp.core"))
  .settings(
    libraryDependencies ++= Seq(
      "dev.zio"                %% "zio"                     % "1.0.0-RC21",
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.6"
    )
  )
  .enablePlugins(BuildInfoPlugin)

lazy val firestore = project
  .in(file("modules/firestore"))
  .dependsOn(core)
  .settings(stdSettings("zio-gcp-firestore"))
  .settings(buildInfoSettings("zio.gcp.firestore"))
  .settings(
    libraryDependencies ++= Seq(
      "dev.zio"                %% "zio"                     % "1.0.0-RC21",
      "dev.zio"                %% "zio-interop-guava"       % "29.0.0.0",
      "com.google.cloud"        % "google-cloud-firestore"  % "1.32.4",
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.6"
    )
  )
  .enablePlugins(BuildInfoPlugin)

lazy val pubsub = project
  .in(file("modules/pubsub"))
  .dependsOn(core)
  .settings(stdSettings("zio-gcp-pubsub"))
  .settings(buildInfoSettings("zio.gcp.pubsub"))
  .settings(
    libraryDependencies ++= Seq(
      "dev.zio"                %% "zio"                     % "1.0.0-RC21",
      "dev.zio"                %% "zio-interop-guava"       % "28.2.0.1",
      "com.google.cloud"        % "google-cloud-pubsub"     % "1.113.5",
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.6"
    )
  )
  .enablePlugins(BuildInfoPlugin)

lazy val storage = project
  .in(file("modules/storage"))
  .dependsOn(core)
  .settings(stdSettings("zio-gcp-storage"))
  .settings(buildInfoSettings("zio.gcp.storage"))
  .settings(
    libraryDependencies ++= Seq(
      "dev.zio"                %% "zio"                     % "1.0.0-RC21",
      "dev.zio"                %% "zio-interop-guava"       % "28.2.0.1",
      "com.google.cloud"        % "google-cloud-storage"    % "1.109.1",
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.6"
    )
  )
  .enablePlugins(BuildInfoPlugin)

lazy val redis = project
  .in(file("modules/redis"))
  .dependsOn(core)
  .settings(stdSettings("zio-gcp-redis"))
  .settings(buildInfoSettings("zio.gcp.redis"))
  .settings(
    libraryDependencies ++= Seq(
      "dev.zio"                %% "zio"                     % "1.0.0-RC21",
      "dev.zio"                %% "zio-interop-guava"       % "28.2.0.1",
      "com.google.cloud"        % "google-cloud-redis"      % "1.0.0",
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.6"
    )
  )
  .enablePlugins(BuildInfoPlugin)
