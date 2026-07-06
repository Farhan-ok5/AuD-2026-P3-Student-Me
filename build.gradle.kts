plugins {
    alias(libs.plugins.algomate)
    alias(libs.plugins.jagr)
}

exercise {
    assignmentId.set("p3")
}

submission {
    // ACHTUNG!
    // Setzen Sie im folgenden Bereich Ihre TU-ID (NICHT Ihre Matrikelnummer!), Ihren Nachnamen und Ihren Vornamen
    // in Anführungszeichen (z.B. "ab12cdef" für Ihre TU-ID) ein!
    studentId = "fa74biza"
    firstName = "Farhan"
    lastName = "Ahmed"
    // Optionally require own tests for mainBuildSubmission task. Default is false
    requireTests = false
    // Optionally require public grader for mainBuildSubmission task. Default is false
    requireGraderPublic = false
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
    implementation("org.tudalgo:algoutils-tutor:0.9.0")
    implementation(libs.algoutils.student)
    implementation(libs.junit.core)
    implementation(libs.junit.pioneer)
}


