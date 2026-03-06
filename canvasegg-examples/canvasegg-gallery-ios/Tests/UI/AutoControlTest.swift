import XCTest

class AutoControlTest: XCTestCase {
    override func setUp() {

    }
    override func tearDown() {

    }
    func testViewAndScroll() {
        let app = XCUIApplication()
        app.launch()
        Thread.sleep(forTimeInterval: 5.0)
        let normalized = app.coordinate(withNormalizedOffset: CGVector(dx: 0, dy: 0))
        let coordinate = normalized.withOffset(CGVector(dx: 294, dy: 184))
        coordinate.tap()
        Thread.sleep(forTimeInterval: 5.0)
    }
}