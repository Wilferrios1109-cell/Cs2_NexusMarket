# Domain Value Objects

## Introduction

Value Objects represent immutable concepts within the NexusMarket domain.

Unlike Entities, Value Objects do not have their own identity. They are defined by their values and are used to encapsulate controlled business concepts.

NexusMarket uses Value Objects primarily for business catalogs such as user roles, user statuses, commercial statuses, product types, product statuses, warehouse types, inventory movement types, and order statuses.

All controlled business catalogs inherit from `DomainCatalog`.

---

# Value Object Hierarchy

```text
DomainCatalog (Abstract)
├── SystemRole
├── UserStatus
├── CommercialStatus
├── ProductType
├── ProductStatus
├── WarehouseType
├── InventoryMovementType
└── OrderStatus
```

---

# DomainCatalog (Abstract)

## Description

Represents a generic business catalog used throughout the NexusMarket domain.

`DomainCatalog` provides a common structure for controlled business values that require a business code, a human-readable name, and a business description.

This class cannot be instantiated directly.

## Attributes

| Attribute | Type | Description |
|---|---|---|
| code | String | Unique business identifier of the catalog value. |
| name | String | Human-readable name displayed within the application. |
| description | String | Business definition of the catalog value. |

## Characteristics

* Immutable.
* Equality is determined by value rather than object identity.
* Catalog values are controlled by the domain.
* Catalog values must not be represented by arbitrary strings throughout the application.
* Each catalog value must have a unique `code`.

---

# SystemRole

## Description

Represents the responsibilities and permissions assigned to a participant within the NexusMarket Marketplace.

Each participant has a single role within the system and may only interact with information according to the responsibilities associated with that role.

The functional specification defines five participants: Buyer, Seller, Logistics Operator, Administrator, and Supervisor.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code | Name | Description |
|---|---|---|
| BUYER | Buyer | Participant who acquires products published in the Marketplace. |
| SELLER | Seller | Participant responsible for registering and managing products. |
| LOGISTICS_OPERATOR | Logistics Operator | Participant responsible for physical warehouse and dispatch operations. |
| ADMINISTRATOR | Administrator | Participant responsible for managing sellers and warehouses. |
| SUPERVISOR | Supervisor | Participant responsible for consultation and operational monitoring. |

## Business Rules

* Each user must have exactly one role.
* A participant may only interact with information according to their assigned role.
* No participant may administer information outside their assigned role.

---

# UserStatus

## Description

Represents the operational status of a user within the NexusMarket Marketplace.

The functional specification defines User Status as a catalog and gives examples such as Active and Blocked.

## Inherits From

`DomainCatalog`

## Allowed Values

The functional specification does not provide the complete official list of values for this catalog.

Therefore, the final list of allowed values must be confirmed before implementation.

Examples explicitly mentioned in the specification include:

| Code | Name | Description |
|---|---|---|
| ACTIVE | Active | User is operational within the Marketplace. |
| BLOCKED | Blocked | User is blocked from normal operation. |

## Business Rules

* User status must be represented by a controlled catalog value.
* Arbitrary strings must not be used to represent user status.

---

# CommercialStatus

## Description

Represents the commercial condition of a Buyer for participating in purchase processes.

The functional specification defines Commercial Status as a mandatory attribute of the Buyer.

## Inherits From

`DomainCatalog`

## Allowed Values

The functional specification identifies the concept but does not define the complete official list of values.

Therefore, the final set of allowed values must be confirmed before implementation.

## Business Rules

* Commercial status must be represented by a controlled catalog value.
* Commercial status determines the condition of the Buyer for participating in purchase processes.

---

# ProductType

## Description

Represents the type of product offered through the NexusMarket catalog.

The Marketplace explicitly distinguishes between Physical and Digital products.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code | Name | Description |
|---|---|---|
| PHYSICAL | Physical | Product that requires inventory and physical dispatch. |
| DIGITAL | Digital | Product delivered immediately after payment. |

## Business Rules

* Physical products require inventory.
* Physical products require physical dispatch.
* Digital products do not require physical dispatch.
* Digital products are delivered immediately after payment.

---

# ProductStatus

## Description

Represents the current publication status of a product within the NexusMarket catalog.

The functional specification defines three product statuses.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code | Name | Description |
|---|---|---|
| PUBLISHED | Published | Product is visible in the public catalog. |
| SUSPENDED | Suspended | Product is temporarily suspended. |
| DISCONTINUED | Discontinued | Product is no longer offered through the catalog. |

## Business Rules

* Product status must be a valid catalog value.
* Product publication must respect the current product status.

---

# WarehouseType

## Description

Represents the classification of a warehouse according to its ownership within NexusMarket.

The functional specification distinguishes between Marketplace warehouses and Seller warehouses.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code | Name | Description |
|---|---|---|
| MARKETPLACE | Marketplace Warehouse | Warehouse belonging to the Marketplace. |
| SELLER | Seller Warehouse | Warehouse associated with a Seller. |

---

# InventoryMovementType

## Description

Represents the type of movement performed on inventory.

The functional specification explicitly defines five inventory movements.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code | Name | Description |
|---|---|---|
| ENTRY | Entry | Inventory entering available stock. |
| RESERVATION | Reservation | Inventory reserved for a commercial process. |
| SALE_OUTPUT | Sale Output | Inventory reduction caused by a sale. |
| ADJUSTMENT | Adjustment | Inventory modification resulting from an adjustment. |
| RETURN | Return | Inventory movement generated by a product return. |

## Business Rules

* Every inventory movement must have a valid movement type.
* Inventory cannot become negative under any circumstance.
* Inventory must be associated with a specific product and warehouse.
* Inventory that does not exist or is marked as damaged cannot be reserved.

---

# OrderStatus

## Description

Represents the current state of an order within the NexusMarket commercial lifecycle.

The functional specification defines the following order lifecycle.

## Inherits From

`DomainCatalog`

## Allowed Values

| Code | Name | Description |
|---|---|---|
| CART | Cart | Provisional selection of products. |
| PENDING_PAYMENT | Pending Payment | Order is waiting for financial confirmation. |
| PAID | Paid | Payment has been confirmed and preparation can begin. |
| DISPATCHED | Dispatched | Order has physically left the warehouse. |
| DELIVERED | Delivered / Completed | Delivery has been successfully completed. |

## Lifecycle

```text
CART
   │
   ▼
PENDING_PAYMENT
   │
   ▼
PAID
   │
   ▼
DISPATCHED
   │
   ▼
DELIVERED / COMPLETED
```

## Business Rules

* An order must follow the defined lifecycle.
* Payment confirmation is required before preparation and logistics processes begin.
* A completed order cannot be modified under any circumstance.

---

# Primitive Enumerations

No additional primitive enumerations are currently defined from the NexusMarket functional specification.

The business concepts explicitly identified as catalogs are represented through `DomainCatalog` Value Objects.

Technical enumerations should only be introduced if a later design requirement identifies a fixed technical concept that does not require business catalog metadata.

---

# Value Object Design Rules

## Immutability

All Value Objects must be immutable after creation.

Their values cannot be modified after the object has been instantiated.

## Equality

Value Objects are compared according to their values rather than object identity.

Two instances containing the same business values represent the same Value Object.

## Controlled Values

Business catalogs must use controlled values defined by the domain.

The application must avoid replacing these concepts with arbitrary strings such as:

```text
"BUYER"
"SELLER"
"PHYSICAL"
"DIGITAL"
"PUBLISHED"
"PAID"
```

throughout the codebase.

Instead, the corresponding Value Objects must be used:

```text
SystemRole
ProductType
ProductStatus
OrderStatus
```

## Business Versus Technical Enumerations

A business concept should be modeled as a `DomainCatalog` Value Object when it requires:

* a business code;
* a display name;
* a business description;
* controlled domain evolution.

A simple enumeration should only be used for fixed technical concepts that do not require business catalog metadata.

## Relationship With Entities

Entities reference Value Objects rather than primitive strings whenever the referenced value represents a controlled business concept.

Examples:

```text
User.role : SystemRole
User.status : UserStatus
Buyer.commercialStatus : CommercialStatus
Product.productType : ProductType
Product.status : ProductStatus
Warehouse.warehouseType : WarehouseType
Inventory.movementType : InventoryMovementType
Order.status : OrderStatus
```

This approach improves type safety, domain expressiveness, maintainability, and consistency with Domain-Driven Design principles.

---

# Domain Validation Rules

## User

* Every user must have exactly one role.
* User status must be a valid catalog value.
* User identification must be unique.
* User email must be unique.

## Buyer

* Commercial status must be a valid catalog value.
* Main address is mandatory.
* Additional addresses are optional.
* A Buyer cannot administer information belonging to other Buyers.
* A Buyer cannot administer inventory.

## Seller

* Sellers cannot self-register.
* Sellers must be incorporated by an Administrator.
* A Seller may only administer its own products and information according to its role.

## Product

* Every product must have a valid product type.
* Product type must be either Physical or Digital.
* Product status must be a valid catalog value.
* Product variants represent differences such as color, size, model, or other characteristics.

## Warehouse

* Every warehouse must have a valid warehouse type.
* A warehouse must be classified as either a Marketplace warehouse or a Seller warehouse.

## Inventory

* Every inventory record must be associated with a Product.
* Every inventory record must be associated with a specific Warehouse.
* Inventory quantity cannot be negative.
* Inventory movements must use a valid `InventoryMovementType`.
* Inventory that does not exist or is marked as damaged cannot be reserved.

## Order

* Every order must have a valid `OrderStatus`.
* Orders must follow the defined lifecycle.
* Payment confirmation is required before preparation and logistics processes begin.
* A completed order cannot be modified.
