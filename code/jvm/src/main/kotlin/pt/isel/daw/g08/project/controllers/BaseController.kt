package pt.isel.daw.g08.project.controllers

import org.springframework.beans.factory.annotation.Autowired
import pt.isel.daw.g08.project.utils.EnvironmentHelper

abstract class BaseController {

    @Autowired
    protected lateinit var env: EnvironmentHelper
}