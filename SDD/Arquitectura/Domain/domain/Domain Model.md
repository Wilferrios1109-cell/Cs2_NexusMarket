# Domain Model

## Introduction

The Domain Model represents the core business entities of the NexusMarket Marketplace.

These entities encapsulate the business information, relationships, rules, and lifecycle concepts described in the functional business specification.

The model follows Object-Oriented Design and Domain-Driven Design (DDD) principles. Inheritance is used to represent genuine domain specialization, while explicit relationships are preferred over generic identifier fields.

The model represents the following main business concepts:

* **Users**, which represent the people authorized to interact with the Marketplace.
* **Buyers**, which represent participants who acquire products.
* **Sellers**, which represent participants responsible for commercializing products.
* **Logistics Operators**, which represent participants responsible for physical warehouse and dispatch operations.
* **Administrators**, which represent participants responsible for seller and warehouse administration.
* **Supervisors**, which represent participants responsible for consultation and operational monitoring.
* **Products**, which represent physical or digital goods offered through the Marketplace.
* **Warehouses**, which represent physical storage locations.
* **Inventory**, which represents the stock available for commercialization.
* **Shopping Carts**, which represent the provisional selection of products.
* **Orders**, which represent formal purchase commitments.
* **Invoices**, which represent commercial billing information associated with purchases.
* **Shipments**, which represent logistics processes for physical products.
* **Returns**, which represent product return processes.
* **Refunds**, which represent refund processes associated with commercial transactions.

The functional specification defines five participants within the Marketplace: Buyer, Seller, Logistics Operator, Administrator, and Supervisor.

Each participant has a single role and may only interact with information according to the responsibilities associated with that role.

---

# Domain Class Hierarchy

```text
User (Abstract)
├── Buyer
├── Seller
├── LogisticsOperator
├── Administrator
└── Supervisor

Product (Abstract)
├── PhysicalProduct
└── DigitalProduct

Warehouse
Inventory
ShoppingCart
Order
Invoice
Shipment
Return
Refund
```

---

# Domain Relationships

```text
User
   │
   ├── Buyer
   │      ├── owns ───────────────> ShoppingCart
   │      └── creates ────────────> Order
   │
   ├── Seller
   │      └── manages ────────────> Product
   │
   ├── LogisticsOperator
   │      ├── manages ────────────> Warehouse
   │      └── manages ────────────> Shipment
   │
   ├── Administrator
   │      ├── registers ──────────> Seller
   │      └── registers ──────────> Warehouse
   │
   └── Supervisor
          └── consults ───────────> Marketplace information

Seller
   │
   └── manages ───────────────────> Product

Product
   │
   ├── PhysicalProduct
   │      └── requires ───────────> Inventory
   │
   └── DigitalProduct
          └── delivered after ────> Payment

Warehouse
   │
   └── stores ────────────────────> Inventory

Inventory
   │
   └── associated with ───────────> Product

ShoppingCart
   │
   └── contains ──────────────────> Product

Order
   ├── created by ────────────────> Buyer
   ├── generated from ───────────> ShoppingCart
   ├── associated with ───────────> Invoice
   ├── generates ─────────────────> Shipment
   └── may generate ──────────────> Return

Return
   │
   └── may generate ──────────────> Refund
```

---

# Entities

---

# User (Abstract)

## Description

Represents any person authorized to interact with the NexusMarket Marketplace.

This abstract class centralizes the common identification and operational information shared by all Marketplace participants.

The functional specification defines the following participants:

* Buyer
* Seller
* Logistics Operator
* Administrator
* Supervisor

Each user has a single role within the system.

This class cannot be instantiated directly.

## Attributes

| Attribute | Type | Description |
|---|---|---|
| identification | String | Unique identifier of the user. |
| fullName | String | Official name of the user. |
| email | String | Primary means of access and communication. |
| role | SystemRole | Defines the responsibilities and permissions of the user. |
| status | UserStatus | Represents the operational condition of the user. |

## Relationships

* A `User` has exactly one `SystemRole`.
* A `User` has one `UserStatus`.
* A `User` may be specialized as a `Buyer`, `Seller`, `LogisticsOperator`, `Administrator`, or `Supervisor`.

## Business Rules

* Every user must have a unique identification.
* Every user must have a unique email.
* Every user must have exactly one role.
* No participant may administer information outside their assigned role.
* Every operation must be executed by an authenticated user.

---

# Buyer

## Description

Represents a participant who acquires products published through the NexusMarket Marketplace.

The Buyer participates in commercial processes such as selecting products through the shopping cart and confirming orders.

The Buyer cannot administer information belonging to other Buyers or inventory.

## Inherits From

`User`

## Attributes

| Attribute | Type | Description |
|---|---|---|
| mainAddress | String | Main location normally used for deliveries. |
| additionalAddresses | List<String> | Additional delivery locations registered by the Buyer. |
| commercialStatus | CommercialStatus | Condition of the Buyer for participating in purchase processes. |

## Relationships

* A `Buyer` owns a `ShoppingCart`.
* A `Buyer` may create multiple `Order` instances.

## Business Rules

* The main address is mandatory.
* Additional addresses are optional.
* Commercial status is mandatory.
* A Buyer cannot administer information belonging to other Buyers.
* A Buyer cannot administer inventory.

---

# Seller

## Description

Represents a participant responsible for registering and managing the products they commercialize through the Marketplace.

Sellers cannot register themselves. They are incorporated into the Marketplace by an Administrator.

## Inherits From

`User`

## Relationships

* A `Seller` manages its own `Product` instances.
* A Seller may have associated warehouses according to the warehouse classification defined by the domain.

## Business Rules

* Sellers cannot self-register.
* Sellers are incorporated by an Administrator.
* A Seller may only administer its own products and information according to its role.

---

# LogisticsOperator

## Description

Represents the participant responsible for the physical logistics operation of the Marketplace.

The Logistics Operator is responsible for activities related to warehouses and dispatches.

## Inherits From

`User`

## Relationships

* A `LogisticsOperator` manages `Warehouse` operations.
* A `LogisticsOperator` manages `Shipment` operations.

---

# Administrator

## Description

Represents the participant responsible for the administrative management of Sellers and Warehouses.

The Administrator incorporates Sellers into the Marketplace and registers their first warehouse.

## Inherits From

`User`

## Relationships

* An `Administrator` registers `Seller` instances.
* An `Administrator` registers `Warehouse` instances.

## Business Rules

* Sellers cannot register themselves.
* The Administrator registers the Seller and the Seller's first warehouse.

---

# Supervisor

## Description

Represents the participant responsible for consultation and operational monitoring within NexusMarket.

The Supervisor has a consultation-oriented role and may access administrative information according to the responsibilities defined by the system.

## Inherits From

`User`

## Relationships

* A `Supervisor` consults Marketplace information.
* The Supervisor does not administer information outside the responsibilities defined for the role.

---

# Product (Abstract)

## Description

Represents a product offered through the NexusMarket catalog.

The Marketplace distinguishes between physical and digital products.

Physical products require inventory and physical dispatch, while digital products are delivered immediately after payment.

This class cannot be instantiated directly.

## Attributes

| Attribute | Type | Description |
|---|---|---|
| productType | ProductType | Defines whether the product is Physical or Digital. |
| variants | List<String> | Represents differences such as color, size, model, or other characteristics. |
| status | ProductStatus | Current publication status of the product. |

## Relationships

* A `Product` may have multiple variants.
* A `Product` may be specialized as a `PhysicalProduct` or `DigitalProduct`.
* A `Product` may be associated with inventory when it is a physical product.

---

# PhysicalProduct

## Description

Represents a physical product offered through the NexusMarket catalog.

Physical products require inventory and physical dispatch.

## Inherits From

`Product`

## Relationships

* A `PhysicalProduct` requires `Inventory`.
* A `PhysicalProduct` may generate a `Shipment` as part of an order.

---

# DigitalProduct

## Description

Represents a digital product offered through the NexusMarket catalog.

Digital products do not require physical dispatch and are delivered immediately after payment.

## Inherits From

`Product`

## Business Rules

* A Digital Product does not require physical inventory.
* A Digital Product does not require physical dispatch.
* A Digital Product is delivered immediately after payment.

---

# Warehouse

## Description

Represents a physical storage location used for inventory management within NexusMarket.

The functional specification distinguishes between Marketplace warehouses and Seller warehouses.

## Attributes

| Attribute | Type | Description |
|---|---|---|
| warehouseType | WarehouseType | Identifies whether the warehouse belongs to the Marketplace or a Seller. |

## Relationships

* A `Warehouse` stores `Inventory`.
* A warehouse may be associated with a Seller when it is classified as a Seller warehouse.

---

# Inventory

## Description

Represents the stock available for commercialization.

Inventory is distributed and must be linked to a specific Product and a specific Warehouse.

The system must not allow negative inventory under any circumstance.

## Attributes

| Attribute | Type | Description |
|---|---|---|
| quantity | Integer | Number of units available in the inventory. |
| movementType | InventoryMovementType | Type of inventory movement being performed. |

## Relationships

* `Inventory` is associated with one `Product`.
* `Inventory` is associated with one `Warehouse`.

## Business Rules

* Inventory must be associated with a Product.
* Inventory must be associated with a specific Warehouse.
* Inventory cannot have negative quantities.
* Inventory movements include Entry, Reservation, Sale Output, Adjustment, and Return.
* Inventory that does not exist or is marked as damaged cannot be reserved.

---

# ShoppingCart

## Description

Represents the provisional selection of products made by a Buyer before confirming a purchase.

The Shopping Cart is part of the commercial purchasing process and precedes the formal Order.

## Attributes

| Attribute | Type | Description |
|---|---|---|
| products | List<Product> | Products selected provisionally by the Buyer. |

## Relationships

* A `ShoppingCart` belongs to one `Buyer`.
* A `ShoppingCart` contains one or more selected `Product` instances.

---

# Order

## Description

Represents the formal commercial commitment generated by a Buyer.

The Order follows a defined lifecycle that represents the central commercial process of the Marketplace.

## Attributes

| Attribute | Type | Description |
|---|---|---|
| status | OrderStatus | Current state of the order within its lifecycle. |

## Relationships

* An `Order` is created by one `Buyer`.
* An `Order` is generated from a `ShoppingCart`.
* An `Order` is associated with an `Invoice`.
* An `Order` generates a `Shipment` when physical products require delivery.
* An `Order` may generate a `Return`.

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

* An Order must follow the defined lifecycle.
* Payment confirmation is required before preparation and logistics processes begin.
* A completed Order cannot be modified under any circumstance.

---

# Invoice

## Description

Represents the commercial billing information associated with purchases made through NexusMarket.

Facturation is part of the commercial operation managed by the Marketplace.

## Relationships

* An `Invoice` is associated with an `Order`.

---

# Shipment

## Description

Represents the logistics process associated with the delivery of physical products.

The logistics process includes preparation, packaging, dispatch, transportation, and delivery.

## Relationships

* A `Shipment` is associated with an `Order`.
* Shipments are applicable to physical products.

---

# Return

## Description

Represents the process through which a product associated with a purchase is returned.

Return management is included within the scope of NexusMarket as part of the post-sale process.

## Relationships

* A `Return` is associated with an `Order`.
* A `Return` may generate a `Refund`.

---

# Refund

## Description

Represents the refund process associated with a commercial transaction.

Refund management is included within the scope of NexusMarket as part of the post-sale process.

## Relationships

* A `Refund` may be generated by a `Return`.

---

# Domain Lifecycle Relationship

The general lifecycle of the NexusMarket commercial process is:

```text
Buyer
   │
   │ selects products
   ▼
ShoppingCart
   │
   │ confirms purchase
   ▼
Order
   │
   │ payment confirmation
   ▼
PAID
   │
   │ preparation and logistics
   ▼
Shipment
   │
   │ delivery confirmation
   ▼
DELIVERED / COMPLETED
```

For digital products, the logistics process is not required:

```text
DigitalProduct
      │
      │ payment confirmed
      ▼
Immediate Delivery
```

For physical products:

```text
PhysicalProduct
      │
      │ inventory reservation
      ▼
Inventory
      │
      │ preparation
      ▼
Shipment
      │
      │ delivery
      ▼
Order Completed
```

---

# Domain Design Rules

## User and Roles

* `User` is an abstract class.
* Every user has exactly one role.
* The available roles are Buyer, Seller, Logistics Operator, Administrator, and Supervisor.
* Participants may only administer information according to their assigned role.
* Every operation must be executed by an authenticated user.

## Seller Management

* Sellers cannot self-register.
* Sellers are incorporated by an Administrator.
* The Administrator registers the Seller and the Seller's first warehouse.

## Product Management

* `Product` is an abstract class.
* Products are classified as Physical or Digital.
* Physical products require inventory and physical dispatch.
* Digital products are delivered immediately after payment.
* Product variants represent differences such as color, size, model, or other characteristics.
* Product status is controlled by the domain.

## Warehouse Management

* Warehouses are classified as Marketplace warehouses or Seller warehouses.
* Warehouses are used for physical inventory management.

## Inventory

* Inventory must be associated with a Product.
* Inventory must be associated with a specific Warehouse.
* Inventory cannot become negative.
* Inventory movements include Entry, Reservation, Sale Output, Adjustment, and Return.
* Inventory that does not exist or is marked as damaged cannot be reserved.

## Orders

* Orders follow the lifecycle:
  * Cart
  * Pending Payment
  * Paid
  * Dispatched
  * Delivered / Completed
* Payment confirmation is required before preparation and logistics processes begin.
* A completed Order cannot be modified.

## Commercial Process

The Marketplace process follows this general sequence:

1. Seller incorporation.
2. Product registration.
3. Inventory registration.
4. Product publication.
5. Product selection through the Shopping Cart.
6. Order confirmation.
7. Payment validation.
8. Preparation and logistics.
9. Delivery confirmation.
10. Order completion.
11. Return and refund processes when applicable.
