package com.hcms.spacex.repo

import com.hcms.spacex.repo.local.domain.CompanyInfoDomain
import com.hcms.spacex.repo.local.domain.LaunchItemDomain
import com.hcms.spacex.repo.remote.dto.CompanyInfoDTO
import com.hcms.spacex.repo.remote.dto.LaunchItemDTO
import com.hcms.spacex.repo.remote.dto.Links
import com.hcms.spacex.repo.remote.dto.Rocket
import io.mockk.every
import io.mockk.mockk

object TestHelper {

    fun commonSetupMockCompanyInfoDTO(downloadedCompanyInfo: CompanyInfoDTO) {
        every { downloadedCompanyInfo.summary } returns "SpaceX designs, manufactures and launches advanced rockets and spacecraft. The company was founded in 2002 to revolutionize space technology, with the ultimate goal of enabling people to live on other planets."
        every { downloadedCompanyInfo.coo } returns "Gwynne Shotwell"
        every { downloadedCompanyInfo.founder } returns "Elon Musk"
        every { downloadedCompanyInfo.founded } returns 2002
        every { downloadedCompanyInfo.vehicles } returns 3
        every { downloadedCompanyInfo.ceo } returns "Elon Musk"
        every { downloadedCompanyInfo.launchSites } returns 3
        every { downloadedCompanyInfo.headquarters } returns null
        every { downloadedCompanyInfo.valuation } returns 27500000000
        every { downloadedCompanyInfo.name } returns "SpaceX"
        every { downloadedCompanyInfo.employees } returns 7000
        every { downloadedCompanyInfo.testSites } returns 1
        every { downloadedCompanyInfo.cto } returns "Elon Musk"
        every { downloadedCompanyInfo.ctoPropulsion } returns "Tom Mueller"

    }

    fun commonSetupMockCompanyInfoDomain(mock: CompanyInfoDomain) {
        every { mock.founder } returns "Elon Musk"
        every { mock.founded } returns 2002
        every { mock.launchSites } returns 3
        every { mock.valuation } returns 27500000000
        every { mock.name } returns "SpaceX"
        every { mock.employees } returns 7000
        every { mock.modifiedAt } returns 0L
    }

    fun commonSetupMockLaunchItemDTO(mock: LaunchItemDTO) {
        val rocket: Rocket = mockk()
        val links: Links = mockk()
        every { mock.missionName } returns "some name"
        every { mock.launchYear } returns "2021"
        every { mock.launchDateUtc } returns "2006-03-24T22:30:00.000Z"
        every { mock.launchDateUnix } returns 1143239400
        every { mock.rocket } returns rocket
        every { rocket.rocketType } returns "Merlin A"
        every { rocket.rocketName } returns "Falcon 1"
        every { mock.launchSuccess } returns false
        every { mock.links } returns links
        every { links.wikipedia } returns "https://en.wikipedia.org/wiki/DemoSat"
        every { links.missionPatchSmall } returns "https://images2.imgbox.com/3c/0e/T8iJcSN3_o.png"
        every { links.videoLink } returns "https://www.youtube.com/watch?v=0a_00nJ_Y88"
        every { mock.upcoming } returns false
    }

    fun commonSetupMockLaunchItemDomain(mock: LaunchItemDomain) {
        every { mock.missionName } returns "some name"
        every { mock.launchYear } returns "2021"
        every { mock.launchDateUtc } returns "2006-03-24T22:30:00.000Z"
        every { mock.launchDateUnix } returns 1143239400
        every { mock.rocketType } returns "Merlin A"
        every { mock.rocketName } returns "Falcon 1"
        every { mock.launchSuccess } returns false
        every { mock.wikipedia } returns "https://en.wikipedia.org/wiki/DemoSat"
        every { mock.missionPatchSmall } returns "https://images2.imgbox.com/3c/0e/T8iJcSN3_o.png"
        every { mock.videoLink } returns "https://www.youtube.com/watch?v=0a_00nJ_Y88"
        every { mock.upcoming } returns false
        every { mock.modifiedAt } returns 0L
    }
}