# Domain Value Objects

## 1. Purpose

This document defines value objects and controlled domain values used by NexusMarket.

A **Value Object** has no independent identity. It is immutable and is compared by its value. An **Entity** has identity and may change state through controlled operations.

The current implementation uses `DomainCatalog` as a base for business catalogs. This document distinguishes:
- **Catalog Value Objects:** controlled values with code, name and description.
- **Validated Value Objects:** immutable values such as email, money, quantity and address.
- **Enumerations:** fixed alternatives whose members do not need catalog metadata.

> Catalog values listed in the functional specification are requirements. Additional value objects below are design proposals to validate before implementation.

## 2. Value Object Hierarchy

```text
DomainCatalog (abstract, immutable)
├── SystemRole
├── UserStatus
├── CommercialStatus
├── ProductType
├── ProductStatus
├── WarehouseType
├── InventoryMovementType
├── OrderStatus
├── PaymentStatus (proposed)
├── ShipmentStatus (proposed)
├── ReturnStatus (proposed)
└── RefundStatus (proposed)

Validated Value Objects (proposed)
├── EmailAddress
├── IdentityDocument
├── PersonName
├── Address
├── Money
├── Quantity
├── SKU
└── TrackingNumber (optional)
```

Identifiers such as `UserId`, `ProductId`, `OrderId` and `WarehouseId` may be modeled as typed Value Objects if doing so improves type safety and validation. Their implementation is a design decision; do not introduce them merely for ceremony.

## 3. DomainCatalog (abstract)

### Purpose
Provides common metadata for a controlled business catalog value.

### Attributes
| Attribute | Type | Rule |
|---|---|---|
| `code` | String | Required, normalized, nonblank |
| `name` | String | Required, nonblank |
| `description` | String | Required or optional according to catalog policy |

### Rules
- Cannot be instantiated directly.
- Must be immutable after construction.
- A catalog's `code` identifies its value within that catalog.
- Equality and hash code must use the same identity semantics. Recommended: same concrete catalog type + same `code`; do not include mutable/display metadata (`name`, `description`) in equality.
- Catalog definitions must be controlled by the domain; callers must not create arbitrary values where only predefined values are allowed.
- Do not use unrestricted setters.

## 4. Catalog Value Objects

### 4.1 SystemRole

**Purpose:** Describes a participant's role.

| Code | Name | Meaning |
|---|---|---|
| `BUYER` | Buyer | Acquires products |
| `SELLER` | Seller | Commercializes products |
| `LOGISTICS_OPERATOR` | Logistics Operator | Warehouse and dispatch operations |
| `ADMINISTRATOR` | Administrator | Seller and initial warehouse administration |
| `SUPERVISOR` | Supervisor | Consultation and monitoring |

Rules:
- One role per User.
- In the current class hierarchy, concrete User subtype determines role; avoid storing a role that can contradict the subtype.
- Role-based authorization belongs to the application/security layer.

### 4.2 UserStatus

The specification mentions Active and Blocked but does not establish the complete official catalog.

| Code | Name | Meaning |
|---|---|---|
| `ACTIVE` | Active | User may operate subject to role permissions |
| `BLOCKED` | Blocked | User cannot perform normal operations |

**Pending:** confirm whether more statuses are required and define allowed transitions.

### 4.3 CommercialStatus

Represents a Buyer's commercial eligibility.

The specification requires the concept but does not define all values or their effects.

**Pending:** define allowed codes (for example, eligible/ineligible only if approved) and what each status permits. Do not invent catalog constants or business behavior.

### 4.4 ProductType

| Code | Name | Meaning |
|---|---|---|
| `PHYSICAL` | Physical | Requires stock and physical fulfillment |
| `DIGITAL` | Digital | Delivered after payment without physical dispatch |

The current `PhysicalProduct` and `DigitalProduct` subclasses already express this specialization. Avoid redundant mutable type state if subtype determines product type.

### 4.5 ProductStatus

| Code | Name | Meaning |
|---|---|---|
| `PUBLISHED` | Published | Eligible for catalog visibility, subject to other rules |
| `SUSPENDED` | Suspended | Temporarily unavailable |
| `DISCONTINUED` | Discontinued | No longer offered |

Rules:
- Product status is controlled.
- Transitions should be exposed through domain operations, not unrestricted setters.
- Whether discontinued products can be reactivated is pending policy.

### 4.6 WarehouseType

| Code | Name | Meaning |
|---|---|---|
| `MARKETPLACE` | Marketplace Warehouse | Belongs to Marketplace |
| `SELLER` | Seller Warehouse | Associated with a Seller |

Rules:
- A Seller warehouse must have an associated Seller.
- A Marketplace warehouse must not be assigned to an individual Seller.

### 4.7 InventoryMovementType

| Code | Name | Meaning |
|---|---|---|
| `ENTRY` | Entry | Stock enters inventory |
| `RESERVATION` | Reservation | Stock is reserved |
| `SALE_OUTPUT` | Sale Output | Stock leaves due to sale |
| `ADJUSTMENT` | Adjustment | Controlled correction |
| `RETURN` | Return | Stock movement caused by a return |

Rules:
- Movement type does not replace validation of quantity or stock.
- Each movement must have positive quantity.
- The stock balance and movement record must be updated consistently.
- Damaged-stock handling and adjustment authorization require policy definition.

### 4.8 OrderStatus

Recommended lifecycle based on the specification:

| Code | Name | Meaning |
|---|---|---|
| `PENDING_PAYMENT` | Pending Payment | Waiting for payment confirmation |
| `PAID` | Paid | Payment confirmed |
| `DISPATCHED` | Dispatched | Physical order dispatched |
| `DELIVERED` | Delivered | Delivery confirmed/completed |

**Correction:** `CART` is not an OrderStatus. `ShoppingCart` is a separate entity, and checkout creates an Order.

For digital-only orders, the exact terminal state and transition must be confirmed. A dedicated `COMPLETED` state may be needed if `DELIVERED` does not represent digital fulfillment adequately.

### 4.9 Proposed lifecycle catalogs

These catalogs are proposed because the specification describes processes but does not provide complete allowed values.

**PaymentStatus**
- `PENDING`
- `CONFIRMED`
- `FAILED`
- `CANCELLED`
- `REFUNDED` (only if full-refund semantics are defined; otherwise track refunds separately)

**ShipmentStatus**
- `PENDING`
- `PREPARING`
- `DISPATCHED`
- `IN_TRANSIT` (if tracking is supported)
- `DELIVERED`
- `CANCELLED`

**ReturnStatus**
- `REQUESTED`
- `UNDER_REVIEW`
- `APPROVED`
- `REJECTED`
- `RECEIVED`
- `COMPLETED`
- `CANCELLED`

**RefundStatus**
- `PENDING`
- `APPROVED`
- `PROCESSING`
- `COMPLETED`
- `FAILED`
- `CANCELLED`

These values must be reviewed against business policy before implementation.

## 5. Validated Value Objects (proposals)

### 5.1 EmailAddress
- Stores a normalized email value.
- Rejects null/blank and values that fail the chosen validation rule.
- Equality uses normalized value.
- Email uniqueness is enforced across users by application/persistence, not by the Value Object alone.

### 5.2 IdentityDocument
- Stores document type and number (or a single normalized identifier if the project scope is simplified).
- Rejects blank values.
- Uniqueness is enforced at system level.
- Colombian document types and validation rules must be agreed before adding them.

### 5.3 PersonName
- Stores a nonblank person's name.
- Normalization policy (spaces, capitalization) should not destroy official spelling.

### 5.4 Address
- Represents a delivery address as structured data, not an arbitrary string.
- Candidate fields: recipient, country, department/state, city, address line, additional details, postal code (optional).
- Exact required fields depend on delivery and project scope.
- Address validation must not assume every address has a postal code.

### 5.5 Money
- Stores amount and currency.
- Uses decimal arithmetic (never `double`/`float` for monetary calculations).
- Rejects negative values where the business operation requires a nonnegative/positive amount.
- Addition/subtraction require compatible currencies.
- Rounding policy and supported currency are pending (COP is likely but must be confirmed).

### 5.6 Quantity
- Represents a quantity of units.
- Rejects negative quantities.
- Cart, order and movement quantities must be strictly greater than zero.
- Stock balances may be zero but cannot be negative.
- Integer vs decimal quantities depend on whether products can be sold fractionally; current model assumes integer units.

### 5.7 SKU
- Stores a normalized, nonblank stock-keeping code.
- Equality uses normalized code.
- Uniqueness scope (system-wide or per Seller) is pending.

### 5.8 TrackingNumber
- Stores a nonblank shipment tracking reference.
- Format depends on carrier; do not impose a carrier-specific regex without integration requirements.

## 6. Equality, Immutability and Validation

All Value Objects must:
1. Validate their required values during construction/factory creation.
2. Expose no setters.
3. Be immutable after creation.
4. Implement equality and hash code consistently.
5. Normalize only when normalization preserves business meaning.
6. Avoid containing mutable collections; if a collection is required, copy it defensively and expose an immutable view.
7. Avoid depending on Spring, JPA, MongoDB or HTTP.

For `DomainCatalog`, recommended equality:
- same concrete catalog class;
- same normalized `code`.

For simple Value Objects, equality uses all meaningful value components (e.g. Money amount + currency; Address fields; EmailAddress normalized value).

## 7. Value Objects vs Entities vs Enumerations

| Concept | Recommended classification | Reason |
|---|---|---|
| User | Entity | Stable identity and lifecycle |
| Product | Entity | Identity, publication state |
| ProductVariant | Entity or child entity | SKU/stock/price may have independent identity |
| Warehouse | Entity | Identity and operations |
| InventoryStock | Entity | Mutable balance |
| InventoryMovement | Entity | Historical record with identity |
| ShoppingCart | Entity | Mutable selection |
| CartItem | Child entity/value object | Depends on whether lines need independent identity |
| CustomerOrder | Entity | Lifecycle and identity |
| OrderItem | Snapshot/value object | Immutable purchase detail |
| Payment | Entity | Transaction identity/status |
| Invoice | Entity | Billing record identity |
| Shipment | Entity | Logistics lifecycle |
| ReturnRequest | Entity | Approval/lifecycle |
| Refund | Entity | Financial operation |
| SystemRole | Catalog VO or enum | Current project uses catalog metadata |
| ProductType | Catalog VO or subtype-only | Avoid redundant type state |
| OrderStatus | Catalog VO or enum | Catalog approach already used |
| Money | Value Object | Amount + currency |
| Quantity | Value Object | Validated numeric value |
| EmailAddress | Value Object | Normalized validated value |
| Address | Value Object | Structured immutable location |

## 8. Validation Responsibilities

- **Value Object:** format, normalization and local value constraints.
- **Entity/Aggregate:** state invariants and valid transitions.
- **Domain Service:** operation spanning multiple domain objects.
- **Application Service/Use Case:** orchestration, authorization context and transaction boundary.
- **Repository/Database:** uniqueness and concurrency guarantees across persisted records.

Do not confuse validation of a value's format with checking that the value is unique in the whole system.

## 9. Pending Decisions

Before implementing these values, confirm:
- CommercialStatus allowed values and effects.
- Whether catalog values remain custom classes or become Java enums.
- Supported currency and rounding policy.
- Whether product quantities can be fractional.
- Address fields required for physical delivery.
- SKU uniqueness scope.
- Payment, shipment, return and refund states.
- User document types and validation rules.
- Whether typed identifiers add enough value for the semester scope.
