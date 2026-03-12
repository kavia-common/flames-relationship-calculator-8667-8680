androidApplication {
    namespace = "org.example.app"

    dependencies {
        implementation("org.apache.commons:commons-text:1.11.0")
        implementation(project(":utilities"))

        // JUnit4 for JVM unit test discovery in this template.
        implementation("junit:junit:4.13.2")
    }
}
