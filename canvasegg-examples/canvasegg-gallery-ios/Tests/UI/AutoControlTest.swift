import XCTest

class AutoControlTest: XCTestCase {
    override func setUp() {

    }
    override func tearDown() {

    }
    func testViewAndScroll() {
        let app = XCUIApplication()
        app.launch()
        let normalized = app.coordinate(withNormalizedOffset: CGVector(dx: 0, dy: 0))
        let coordinate = normalized.withOffset(CGVector(dx: 294, dy: 184))
        coordinate.tap()
    }
}