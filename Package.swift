// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorEbroSensors",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "CapacitorEbroSensors",
            targets: ["SensorsPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "SensorsPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/SensorsPlugin"),
        .testTarget(
            name: "SensorsPluginTests",
            dependencies: ["SensorsPlugin"],
            path: "ios/Tests/SensorsPluginTests")
    ]
)