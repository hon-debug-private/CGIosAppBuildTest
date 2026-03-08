import XCTest

class AutoControlTest: XCTestCase {

    let app = XCUIApplication()

    override func setUp() {
        super.setUp()
        continueAfterFailure = false
        app.launch()

        let _ = app.wait(for: .runningForeground, timeout: 300)
    }

    override func tearDown() {
        super.tearDown()
    }

    func testViewAndScroll() {
        XCTAssertTrue(app.exists, "Not application launched")
        Thread.sleep(forTimeInterval: 5.0)
        let normalized = app.coordinate(withNormalizedOffset: CGVector(dx: 0, dy: 0))
        let coordinate = normalized.withOffset(CGVector(dx: 294, dy: 184))


        coordinate.tap()

        Thread.sleep(forTimeInterval: 5.0)
    }
}