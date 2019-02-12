# workshop-devcon-2018
Liferay Commerce extensions used during the Liferay Commerce Workshop at the Liferay DEVCON 2018

This is a complete Liferay Workspace. It can be imported as it is in a Liferay Developer Studio (Eclipse + plugins) or import the OSGi modules within the "modules" folder into any other IDE.

Extensions included:
- _Product Publisher Datasource_ - When selected, it will list the products showing first the last products added to the catalog
- _Product Pubblisher Renderer_ - This includes 2 renderers, based on the carousel component of Bootstrap 4
- _Checkout custom Validator_ - This adds a new validator to the checkout process. It also adds a new custom field to products called "Temporarily Out" so admins can set this to true for any product. The validator will check if a product is marked as temporarily out, in which case the checkout is not possible for those products.

To compile and deploy just use the Gradle Wrapper included in the Liferay Workspace:
>gradlew deploy
